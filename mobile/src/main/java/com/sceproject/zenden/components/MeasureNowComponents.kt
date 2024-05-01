package com.sceproject.zenden.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sceproject.zenden.data.viewmodels.MeasureNowViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

@Composable
fun MeasureNowContent(paddingValues: PaddingValues, viewModel: MeasureNowViewModel) {
    LaunchedEffect(Unit) {
        viewModel.simulateHeartRateMeasurement()
    }

    val measurementStatus by viewModel.measurementStatus.observeAsState(MeasureNowViewModel.MeasurementStatus.Connecting)
    val heartRate by viewModel.heartRate.observeAsState(0)
    val gad7Responses by viewModel.gad7Responses.observeAsState(emptyMap())

    val showDialog = remember { mutableStateOf(false) }
    val totalAnxietyScore = remember { mutableStateOf(0) }

    Surface(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        when (measurementStatus) {
            MeasureNowViewModel.MeasurementStatus.Connecting -> ProgressStateUI("מתחבר לשעון...")
            MeasureNowViewModel.MeasurementStatus.Measuring -> ProgressStateUI("מקבל נתוני דופק...")
            MeasureNowViewModel.MeasurementStatus.Received -> {
                // Scrollable Column for questions
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("קצב דופק: $heartRate BPM", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(20.dp))
                    // Wrapping the questions in a Card for visual distinction
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp
                    ) {
                        // Questions listed here
                        Column(modifier = Modifier.padding(16.dp)) {
                            viewModel.gad7Questions.forEach { question ->
                                Text(question.questionText, style = MaterialTheme.typography.subtitle1)
                                question.answers.forEachIndexed { index, answer ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = gad7Responses[question.id] == index,
                                            onClick = { viewModel.setGad7Response(question.id, question.scores[index]) }
                                        )
                                        Text(answer, style = MaterialTheme.typography.body1)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    Button(
                        onClick = {
                            totalAnxietyScore.value = viewModel.calculateTotalAnxiety(heartRate)
                            showDialog.value = true
                        },
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        Text("חשב", style = MaterialTheme.typography.button)
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        CustomAlertDialog(showDialog, totalAnxietyScore)
    }
}


@Composable
fun CustomAlertDialog(showDialog: MutableState<Boolean>, totalAnxietyScore: MutableState<Int>) {
    // Context is not used in this example, but you might need it for more complex scenarios
    val context = LocalContext.current

    // Construct the message and buttons dynamically based on the anxiety score
    val (message, buttonText, buttonAction) = when {
        totalAnxietyScore.value > 15 -> Triple(
            "ציון החרדה שלך די גבוה, מה שמצביע על לחץ או חרדה משמעותיים. טכניקות הרגעה יועילו.",
            "הרגעה",
            { ZenDenAppRouter.navigateTo(Screen.RelaxationScreen) }
        )
        totalAnxietyScore.value > 10 -> Triple(
            "הציון שלך מרמז על חרדה בינונית. זה עשוי להיות מועיל לעסוק בפעילויות מסוימות כדי להרגע.",
            "הרגעה",
            { ZenDenAppRouter.navigateTo(Screen.RelaxationScreen) } // Assuming you have a StressManagementScreen
        )
        else -> Triple(
            "ציון החרדה שלך נמצא בטווח נורמלי, וזה נהדר! המשך לעקוב אחר רווחתך ולעסוק בפעילויות בריאות.",
            "המשך",
            { ZenDenAppRouter.navigateTo(Screen.HomeScreen) }
        )
    }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text("תוצאת המדידה", style = MaterialTheme.typography.h5) },
        text = { Text(message, style = MaterialTheme.typography.body1) },
        confirmButton = {
            Button(onClick = {
                showDialog.value = false
                buttonAction() // Invoke the action associated with the button
            }) {
                Text(buttonText)
            }
        },
        // Only show the dismissButton if there's an alternative action for lower scores
        dismissButton = if (totalAnxietyScore.value <= 10) null else {
            {
                Button(onClick = { showDialog.value = false }) {
                    Text("חזור")
                }
            }
        }
    )
}

@Composable
fun ProgressStateUI(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = message, style = MaterialTheme.typography.body1)
    }
}
