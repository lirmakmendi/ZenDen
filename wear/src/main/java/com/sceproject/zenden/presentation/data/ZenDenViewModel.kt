package com.sceproject.zenden.presentation.data

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ZenDenViewModel(
    application: Application,
    private val healthServicesRepository: HealthServicesRepository
) : AndroidViewModel(application) {

    val enabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hr: MutableState<Double> = mutableStateOf(0.0)
    val availability: MutableState<DataTypeAvailability> =
        mutableStateOf(DataTypeAvailability.UNKNOWN)
    val progress: MutableState<Int> = mutableStateOf(0)

    val uiState: MutableState<UiState> = mutableStateOf(UiState.Startup)

    init {
        viewModelScope.launch {
            val supported = healthServicesRepository.hasHeartRateCapability()
            uiState.value = if (supported) {
                UiState.Startup
            } else {
                UiState.NotSupported
            }
        }

        viewModelScope.launch {
            enabled.collect {
                if (it) {
                    healthServicesRepository.heartRateMeasureFlow()
                        .takeWhile { enabled.value }
                        .collect { measureMessage ->
                            when (measureMessage) {
                                is MeasureMessage.MeasureData -> {
                                    val lastHeartRate = measureMessage.data.last().value
                                    hr.value = lastHeartRate
                                    if (lastHeartRate > 100) {
                                        toggleEnabled()
                                        uiState.value = UiState.ShowRelaxMessage // Assuming you add this state
                                    }
                                }

                                is MeasureMessage.MeasureAvailability -> {
                                    availability.value = measureMessage.availability
                                }
                            }
                        }
                }
            }
        }
    }

    fun startMeasurement() {
        viewModelScope.launch {
            enabled.value = true
            uiState.value = UiState.Measuring
            for (i in 1..20) {
                delay(1000)
                progress.value = i * 5
            }
            enabled.value = false
            uiState.value = UiState.Result(hr.value)
            sendMessageToPhone(hr.value.toString())  // Send HR value to the phone
        }
    }

    fun toggleEnabled() {
        enabled.value = !enabled.value
        if (!enabled.value) {
            availability.value = DataTypeAvailability.UNKNOWN
        }
    }

    fun goBackFromRelaxMessage() {
        uiState.value = UiState.Supported // or any appropriate state indicating back navigation
    }

    private fun sendMessageToPhone(message: String) {
        val bytes = message.toByteArray()
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "Error sending message: ${exception.localizedMessage}")
        }

        viewModelScope.launch(exceptionHandler) {
            val nodes = Wearable.getNodeClient(getApplication<Application>().applicationContext).connectedNodes.await()
            for (node in nodes) {
                Wearable.getMessageClient(getApplication<Application>().applicationContext).sendMessage(node.id, "/last_hr_measurement", bytes).await()
            }
        }
    }


}

class ZenDenViewModelFactory(
    private val application: Application,
    private val healthServicesRepository: HealthServicesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZenDenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ZenDenViewModel(
                application = application,
                healthServicesRepository = healthServicesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class UiState {
    object Startup : UiState()
    object NotSupported : UiState()
    object Supported : UiState()
    object ShowRelaxMessage : UiState()
    object ShowRelaxTips : UiState()
    object Measuring : UiState()
    data class Result(val hr: Double) : UiState()
}
