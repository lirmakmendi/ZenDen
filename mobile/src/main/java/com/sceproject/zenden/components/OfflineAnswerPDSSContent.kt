package com.sceproject.zenden.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sceproject.zenden.data.viewmodels.OfflineAnswerPDSSViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

@Composable
fun CustomAlertDialog(
    showDialog: MutableState<Boolean>,
    score: Int
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text("תוצאת השאלון", style = MaterialTheme.typography.h5) },
        text = {
            Column {
                Text("הציון הכולל שלך הוא: $score", style = MaterialTheme.typography.body1)
                if (score > 9) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "יש אפשרות להתקף פאניקה!",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        },
        confirmButton = {
            Column {
                Button(onClick = { showDialog.value = false }) {
                    Text("אישור")
                }
                if (score > 9) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
//                        ZenDenAppRouter.navigateTo(Screen.HowToDealWithPanicAttackScreen)
                    }) {
                        Text("איך להתמודד עם התקף פאניקה")
                    }
                }
            }
        }
    )
}

@Composable
fun OfflineAnswerPDSSContent(paddingValues: PaddingValues, viewModel: OfflineAnswerPDSSViewModel) {
    val pdssResponses by viewModel.pdssResponses.observeAsState(emptyMap())
    val totalScore by viewModel.totalScore.observeAsState(0)
    val showDialog = remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    viewModel.pdssQuestions.forEach { question ->
                        Text(
                            question.questionText,
                            style = MaterialTheme.typography.subtitle1
                        )
                        question.answers.forEachIndexed { index, answer ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = pdssResponses[question.id] == question.scores[index],
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
                    viewModel.calculateTotalScore()
                    showDialog.value = true
                },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("חשב ציון", style = MaterialTheme.typography.button)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { ZenDenAppRouter.navigateTo(Screen.LoginScreen) },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("חזור למסך הכניסה", style = MaterialTheme.typography.button)
            }
        }
    }

    if (showDialog.value) {
        CustomAlertDialog(
            showDialog = showDialog,
            score = totalScore
        )
    }
}