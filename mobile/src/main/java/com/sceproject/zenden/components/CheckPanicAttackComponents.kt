package com.sceproject.zenden.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.font.FontWeight


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CheckPanicAttackContent(
    paddingValues: PaddingValues,
    age: String,
    gender: String,
    lastPdssMeasurement: Int,
    lastSdnnMeasurement: Float,
    isLoading: Boolean,
    predictionResult: Float?,
    onHomeClick: () -> Unit,
    onRelaxationClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            EnhancedLoadingIndicator()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text(
                        text = "בדיקת התקף חרדה",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                item {
                    MeasurementCard(
                        label = "תוצאת שאלון אחרונה",
                        value = lastPdssMeasurement.toString()
                    )
                    MeasurementCard(
                        label = "מדידת שינוי קצב לב אחרונה",
                        value = lastSdnnMeasurement.toString()
                    )
                    predictionResult?.let {
                        val result = getPredictionResult(it, onHomeClick, onRelaxationClick)
                        PredictionResultCard(result)
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.size(80.dp)) {
            drawArc(
                color = Color(0xFF6200EE),
                startAngle = 0f,
                sweepAngle = rotation,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
        Text(text = "Calculating...", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun MeasurementCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PredictionResultCard(predictionResult: PredictionResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        backgroundColor = predictionResult.resultColor,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "תוצאה: ${predictionResult.resultText}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = predictionResult.resultTextColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = predictionResult.navigateButtonAction) {
                Text(text = predictionResult.navigateButtonText)
            }
        }
    }
}

fun getPredictionResult(
    prediction: Float,
    onHomeClick: () -> Unit,
    onRelaxationClick: () -> Unit
): PredictionResult {
    return when {
        prediction < 0.4f -> {
            PredictionResult(
                resultText = "לםי החיזוי, אין אינדיקציה להתקף חרדה",
                resultColor = Color.Green,
                resultTextColor = Color.White,
                navigateButtonText = "חזור למסך הבית",
                navigateButtonAction = onHomeClick
            )
        }
        prediction < 0.7f -> {
            PredictionResult(
                resultText = "ייתכן התקף חרדה קל",
                resultColor = Color.Yellow,
                resultTextColor = Color.Black,
                navigateButtonText = "טכניקות הרגעה מהירות",
                navigateButtonAction = onRelaxationClick
            )
        }
        else -> {
            PredictionResult(
                resultText = "ייתכן התקף חרדה חמור",
                resultColor = Color.Red,
                resultTextColor = Color.White,
                navigateButtonText = "מומלץ לפנות לעזרה מקצועית",
                navigateButtonAction = onHomeClick
            )
        }
    }
}


data class PredictionResult(
    val resultText: String,
    val resultColor: Color,
    val resultTextColor: Color,
    val navigateButtonText: String,
    val navigateButtonAction: () -> Unit
)
