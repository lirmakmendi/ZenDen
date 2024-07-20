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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sceproject.zenden.data.viewmodels.AnswerPDSSViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

@Composable
fun CustomAlertDialog(
    showDialog: MutableState<Boolean>,
    viewModel: AnswerPDSSViewModel
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text("הצלחה", style = MaterialTheme.typography.h5) },
        text = { Text("המדידה נשמרה בהצלחה", style = MaterialTheme.typography.body1) },
        confirmButton = {
            Button(onClick = {
                showDialog.value = false
                ZenDenAppRouter.navigateTo(Screen.HomeScreen)
            }) {
                Text("אישור")
            }
        }
    )
}

@Composable
fun AnswerPDSSContent(paddingValues: PaddingValues, viewModel: AnswerPDSSViewModel) {
    val measurementStatus by viewModel.measurementStatus.observeAsState(AnswerPDSSViewModel.MeasurementStatus.Connecting)
    val heartRate by viewModel.heartRate.observeAsState(0)
    val pdssResponses by viewModel.pdssResponses.observeAsState(emptyMap())
    val lastHeartRateTimestamp by viewModel.lastHeartRateTimestamp.observeAsState("")
    val lastPdssMeasurement by viewModel.lastPdssMeasurement.observeAsState(0)
    val lastPdssTimestamp by viewModel.lastPdssTimestamp.observeAsState("")

    val showDialog = remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        when (measurementStatus) {
            AnswerPDSSViewModel.MeasurementStatus.Connecting -> ProgressStateUI("מתחבר לשעון...")
            AnswerPDSSViewModel.MeasurementStatus.Measuring -> ProgressStateUI("מקבל נתוני דופק...")
            AnswerPDSSViewModel.MeasurementStatus.Received -> {
                // Scrollable Column for questions
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("שינוי בקצב הלב: $heartRate m/s", style = MaterialTheme.typography.body1)
                    Text(
                        "זמן מדידת שינוי בקצב הלב האחרון: $lastHeartRateTimestamp",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        "המדידה האחרונה של PDSS: $lastPdssMeasurement",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        "זמן מדידת ה-PDSS האחרון: $lastPdssTimestamp",
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // Wrapping the questions in a Card for visual distinction
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp
                    ) {
                        // Questions listed here
                        Column(modifier = Modifier.padding(16.dp)) {
                            viewModel.pdssQuestions.forEach { question ->
                                Text(
                                    question.questionText,
                                    style = MaterialTheme.typography.subtitle1
                                )
                                question.answers.forEachIndexed { index, answer ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = pdssResponses[question.id] == index,
                                            onClick = {
                                                viewModel.setPdssResponse(
                                                    question.id,
                                                    question.scores[index]
                                                )
                                            }
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
                            val totalPanicDisorderScore =
                                viewModel.calculateTotalPanicDisorder(heartRate)
                            viewModel.savePdssScoreToFirestore(totalPanicDisorderScore)
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
        CustomAlertDialog(showDialog, viewModel)
    }
}

@Composable
fun ProgressStateUI(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = message, style = MaterialTheme.typography.body1)
    }
}
