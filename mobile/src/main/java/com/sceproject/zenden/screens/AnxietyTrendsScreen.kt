package com.sceproject.zenden.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.components.AppScaffold
import kotlin.random.Random


@Composable
fun AnxietyTrendsScreen() {
    // Different sets of data representing different time ranges
    val lastWeekData = listOf(20, 30, 50, 40, 35, 60, 55)
    val lastMonthData = listOf(30, 45, 30, 55, 60, 35, 50, 40, 65, 40, 55, 45, 60, 50, 65)
    val lastYearData = List(12) { Random.nextInt(20, 70) } // Mock data for 12 months

    var currentData by remember { mutableStateOf(lastWeekData) } // Default to last week

    AppScaffold(
        title = "היסטוריית חרדה",
        homeViewModel = viewModel(),
        secondaryViewModel = null,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("רמות החרדה שלך לאורך זמן", style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(20.dp))
                SimplifiedDummyGraph(currentData)
                Spacer(Modifier.height(20.dp))
                TimeRangeFilter(onTimeRangeSelected = { timeRange ->
                    currentData = when (timeRange) {
                        "Last Week" -> lastWeekData
                        "Last Month" -> lastMonthData
                        "Last Year" -> lastYearData
                        else -> lastWeekData
                    }
                })
            }
        }
    )
}


@Composable
fun SimplifiedDummyGraph(dataPoints: List<Int>, modifier: Modifier = Modifier) {
    val graphHeight = 200.dp
    val strokeWidth = 4f
    val textColor = MaterialTheme.colors.onSurface
    val lineColor = MaterialTheme.colors.primary
    val pathEffect = PathEffect.cornerPathEffect(50f)

    Canvas(
        modifier = modifier
            .height(graphHeight)
            .fillMaxWidth()
    ) {
        val maxDataPoint = dataPoints.maxOrNull()?.toFloat() ?: 1f
        val xIncrement = size.width / (dataPoints.size - 1)

        dataPoints.forEachIndexed { index, value ->
            val x = index * xIncrement
            val y = size.height - (value.toFloat() / maxDataPoint * size.height)
            // Draw the line path
            if (index > 0) {
                drawLine(
                    color = lineColor,
                    start = Offset(
                        x - xIncrement,
                        size.height - (dataPoints[index - 1].toFloat() / maxDataPoint * size.height)
                    ),
                    end = Offset(x, y),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect
                )
            }
            // Draw data point value
            drawContext.canvas.nativeCanvas.drawText(
                "$value%",
                x,
                y - 10.dp.toPx(), // Adjust this value for positioning
                Paint().apply {
                    color = android.graphics.Color.parseColor(toHex(textColor))
                    textSize = 30f // Adjust text size as needed
                }
            )
        }
    }
}

// Helper function to convert Color to hex string
fun toHex(color: Color): String = "#${Integer.toHexString(color.toArgb())}"


@Composable
fun TimeRangeFilter(onTimeRangeSelected: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = { onTimeRangeSelected("Last Week") }) { Text("שבוע שעבר") }
        Button(onClick = { onTimeRangeSelected("Last Month") }) { Text("חודש שעבר") }
        Button(onClick = { onTimeRangeSelected("Last Year") }) { Text("שנה שעברה") }
    }
}