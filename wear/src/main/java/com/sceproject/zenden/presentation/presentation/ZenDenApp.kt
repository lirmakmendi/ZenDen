package com.sceproject.zenden.presentation.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.wear.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.sceproject.zenden.presentation.PERMISSION
import com.sceproject.zenden.presentation.data.HealthServicesRepository

import com.sceproject.zenden.presentation.theme.ZenDenTheme
import java.time.LocalTime

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ZenDenApp(
    healthServicesRepository: HealthServicesRepository
) {
    ZenDenTheme {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {
            val viewModel: ZenDenViewModel = viewModel(
                factory = ZenDenViewModelFactory(
                    healthServicesRepository = healthServicesRepository
                )
            )
            val enabled by viewModel.enabled.collectAsState()
            val hr by viewModel.hr
            val availability by viewModel.availability
            val uiState by viewModel.uiState
            when (uiState) {
                UiState.Startup -> {
                    StartingScreen(onStartMeasurement = {
                        // Or change state to move to the measurement screen
                        viewModel.uiState.value = UiState.Supported
                    })
                }


                UiState.Supported -> {
                    val permissionState = rememberPermissionState(
                        permission = PERMISSION,
                        onPermissionResult = { granted ->
                            if (granted) viewModel.toggleEnabled()
                        }
                    )
                    ZenDenScreen(
                        hr = hr,
                        availability = availability,
                        enabled = enabled,
                        onButtonClick = { viewModel.toggleEnabled() },
                        permissionState = permissionState,
                        onBack = { viewModel.uiState.value = UiState.Startup }
                    )
                }

                UiState.NotSupported -> NotSupportedScreen()
                UiState.Startup -> {
                    CircularProgressIndicator()
                }

                UiState.ShowRelaxMessage -> ShowRelaxMessageScreen(
                    onHelpRelax = { viewModel.uiState.value = UiState.ShowRelaxTips },
                    onGoToPhone = { viewModel.goBackFromRelaxMessage() } // Make sure this correctly implements back navigation
                )
                UiState.ShowRelaxTips -> ShowRelaxTipsScreen(
                    onBack = { viewModel.uiState.value = UiState.ShowRelaxMessage }
                )


            }
        }
    }
}


@Composable
fun ShowRelaxMessageScreen(onHelpRelax: () -> Unit, onGoToPhone: () -> Unit) {
    Column {
        TopAppBarWithBackButton(onBack = onGoToPhone)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    "הדופק שלך עלה על 110, האם אתה מרגיש לחוץ?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onHelpRelax,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("הרגעה מהירה")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onGoToPhone, // Assuming this triggers an action for further help
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("הרגעה במכשיר הנייד")
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

@Composable
fun ShowRelaxTipsScreen(onBack: () -> Unit) {
    Column {
        TopAppBarWithBackButton(onBack = onBack)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    "טיפים להרגעה מהירה",
                    style = MaterialTheme.typography.caption1, // Use a larger typography style for the title
                    modifier = Modifier.padding(bottom = 20.dp) // Increase bottom padding for separation
                )
                // Improved readability with larger spacing and text sizes
                Spacer(modifier = Modifier.height(10.dp))
                Text("1. קחו חמש נשימות עמוקות - שאפו עמוק, החזיקו לרגע ונשפו לאט. התמקדו אך ורק בנשימה.", style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(10.dp))
                Text("2. יישם את טכניקת 5-4-3-2-1 - זהה חמישה דברים שאתה יכול לראות, ארבעה דברים שאתה יכול לגעת בהם, שלושה דברים שאתה יכול לשמוע, שני דברים שאתה יכול להריח, ודבר אחד שאתה יכול לטעום.", style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(10.dp))
                Text("3. מתיחה - בצע סדרה מהירה של מתיחות צוואר וכתפיים כדי לשחרר מתח.", style = MaterialTheme.typography.body1)
                // Add more tips with Spacers as needed for readability
            }
        }
    }
}


// Inside your theme setup, typically found in Theme.kt or a similar file
val ZenDenColors = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    // Define other colors as needed
    onPrimary = Color.White, // Text color on primary color background
    surface = Color(0xFF121212), // For background surfaces
    onSurface = Color.White // Text color on surface background
    // Add additional custom colors as required
)


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
