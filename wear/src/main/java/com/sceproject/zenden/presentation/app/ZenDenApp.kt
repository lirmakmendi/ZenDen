package com.sceproject.zenden.presentation.app

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.sceproject.zenden.presentation.PERMISSION
import com.sceproject.zenden.presentation.data.HealthServicesRepository
import com.sceproject.zenden.presentation.data.UiState
import com.sceproject.zenden.presentation.data.ZenDenViewModel
import com.sceproject.zenden.presentation.data.ZenDenViewModelFactory
import com.sceproject.zenden.presentation.screens.NotSupportedScreen
import com.sceproject.zenden.presentation.screens.RelaxMessageScreen
import com.sceproject.zenden.presentation.screens.RelaxTipsScreen
import com.sceproject.zenden.presentation.screens.StartingScreen
import com.sceproject.zenden.presentation.screens.ZenDenScreen
import com.sceproject.zenden.presentation.theme.ZenDenTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ZenDenApp(
    application: Application,
    healthServicesRepository: HealthServicesRepository
) {
    ZenDenTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {
            val viewModel: ZenDenViewModel = viewModel(
                factory = ZenDenViewModelFactory(
                    application = application,
                    healthServicesRepository = healthServicesRepository
                )
            )
            val enabled by viewModel.enabled.collectAsState()
            val hr by viewModel.hr
            val availability by viewModel.availability
            val uiState by viewModel.uiState
            val progress by viewModel.progress

            when (uiState) {
                UiState.Startup -> {
                    StartingScreen(onStartMeasurement = {
                        viewModel.uiState.value = UiState.Supported
                    })
                }

                UiState.Supported -> {
                    val permissionState = rememberPermissionState(
                        permission = PERMISSION,
                        onPermissionResult = { granted ->
                            if (granted) viewModel.startMeasurement()
                        }
                    )
                    ZenDenScreen(
                        hr = hr,
                        availability = availability,
                        enabled = enabled,
                        onButtonClick = { viewModel.startMeasurement() },
                        permissionState = permissionState,
                        onBack = { viewModel.uiState.value = UiState.Startup },
                        progress = progress,
                        uiState = uiState
                    )
                }

                UiState.NotSupported -> NotSupportedScreen()
                UiState.ShowRelaxMessage -> RelaxMessageScreen(
                    onHelpRelax = { viewModel.uiState.value = UiState.ShowRelaxTips },
                    onGoToPhone = { viewModel.goBackFromRelaxMessage() } // Make sure this correctly implements back navigation
                )

                UiState.ShowRelaxTips -> RelaxTipsScreen(
                    onBack = { viewModel.uiState.value = UiState.ShowRelaxMessage }
                )

                UiState.Measuring, is UiState.Result -> {
                    ZenDenScreen(
                        hr = hr,
                        availability = availability,
                        enabled = enabled,
                        onButtonClick = { viewModel.startMeasurement() },
                        permissionState = rememberPermissionState(permission = PERMISSION),
                        onBack = { viewModel.uiState.value = UiState.Startup },
                        progress = progress,
                        uiState = uiState
                    )
                }
            }
        }
    }
}

@Composable
fun TopAppBarWithBackButton(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "חזור")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "התראת דופק מהיר",
            style = MaterialTheme.typography.caption1
        )
    }
}