package com.sceproject.zenden.presentation.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sceproject.zenden.presentation.data.HealthServicesRepository
import com.sceproject.zenden.presentation.data.MeasureMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class ZenDenViewModel(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {
    val enabled: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val hr: MutableState<Double> = mutableStateOf(0.0)
    val availability: MutableState<DataTypeAvailability> =
        mutableStateOf(DataTypeAvailability.UNKNOWN)

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
                                        // Stop measuring and inform user to relax
                                        toggleEnabled() // Assuming this method stops the measurement
                                        uiState.value =
                                            UiState.ShowRelaxMessage // Assuming you add this state
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

    fun toggleEnabled() {
        enabled.value = !enabled.value
        if (!enabled.value) {
            availability.value = DataTypeAvailability.UNKNOWN
        }
    }


    fun goBackFromRelaxMessage() {
        // Adjust based on your logic. For instance, setting the UI state to return to a previous screen.
        uiState.value = UiState.Supported // or any appropriate state indicating back navigation
    }


}

class ZenDenViewModelFactory(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZenDenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ZenDenViewModel(
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
    object ShowRelaxTips : UiState() // New state for showing relaxation tips
}

