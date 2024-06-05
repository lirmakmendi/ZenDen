package com.sceproject.zenden.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import java.time.LocalTime

@Composable
fun StartingScreen(onStartMeasurement: () -> Unit) {
    // Determine the greeting based on the current hour
    val currentTime = LocalTime.now()
    val greeting = when (currentTime.hour) {
        in 0..11 -> "בוקר טוב"
        in 12..16 -> "צהריים טובים"
        else -> "ערב טוב"
    }

    // Placeholder for the last measurement
    val lastMeasurement = 72 // Pretend this is the last recorded heart rate

    Surface(color = MaterialTheme.colors.surface, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "ZenDen",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$greeting, איך אתה מרגיש?",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "מדידה אחרונה",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "$lastMeasurement bpm",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.secondary, // Use the secondary color for emphasis
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Button(
                    onClick = onStartMeasurement,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp), // Add some horizontal padding for aesthetics
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("מדוד עכשיו", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}
