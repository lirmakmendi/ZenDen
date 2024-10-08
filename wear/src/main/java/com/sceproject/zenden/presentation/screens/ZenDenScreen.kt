package com.sceproject.zenden.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.DataTypeAvailability
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.sceproject.zenden.presentation.components.HrLabel
import com.sceproject.zenden.presentation.data.UiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ZenDenScreen(
    hr: Double,
    availability: DataTypeAvailability,
    enabled: Boolean,
    onButtonClick: () -> Unit,
    onBack: () -> Unit, // This lambda is called when the back button is pressed
    permissionState: PermissionState,
    progress: Int,
    uiState: UiState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
            Spacer(modifier = Modifier.weight(1f)) // Pushes the back button to the left and content to the right
            // You can place more content in the header row if needed
        }

        when (uiState) {
            is UiState.Measuring -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(progress = progress / 100f)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "$progress%", style = MaterialTheme.typography.body1)
                }
            }
            is UiState.Result -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HrLabel(
                        hr = hr,
                        availability = availability
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Measurement complete", style = MaterialTheme.typography.body1)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HrLabel(
                        hr = hr,
                        availability = availability
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Add some space between the label and button
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = {
                            if (permissionState.status.isGranted) {
                                onButtonClick()
                            } else {
                                permissionState.launchPermissionRequest()
                            }
                        }
                    ) {
                        Text(if (enabled) "Stop" else "Start") // Adjust the text based on the enabled state
                    }
                }
            }
        }
    }
}
