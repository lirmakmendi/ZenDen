package com.sceproject.zenden.components

import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import java.time.LocalTime
import kotlin.random.Random

@Composable
fun HomeScreenContent(paddingValues: PaddingValues, viewModel: HomeViewModel) {
    val currentAnxietyLevel = remember { mutableStateOf(40f) } // Example dynamic value
    val dailyAnxietySummary = remember { mutableStateOf(listOf(50f, 60f, 70f, 80f, 90f)) }
    val dailyTips = remember {
        mutableStateOf(
            listOf(
                "קחו נשימות עמוקות",
                "שמרו על לוח זמנים קבוע של שינה",
                "התאמנו באופן קבוע",
                "דברו על הרגשות שלכם"
            )
        )
    }
    val mockHeartbeatData = remember { mutableStateOf(generateMockHeartbeatData()) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { FeelingHeader() }
            item { LastHeartbeatCard(mockHeartbeatData.value) }
            item { CurrentAnxietyLevel(currentAnxietyLevel.value) }
            item { DailySummaryGraph(dailyAnxietySummary.value) }
            item { TipsAndAdviceSection(dailyTips.value) }
        }
    }
}


@Composable
fun CurrentAnxietyLevel(anxietyLevel: Float) {
    // Using Material Card for better UI presentation
    Card(modifier = Modifier.padding(16.dp), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "רמת החרדה הנוכחית", style = MaterialTheme.typography.h6)
            LinearProgressIndicator(
                progress = anxietyLevel / 100f,
                color = when {
                    anxietyLevel > 75 -> Color.Red
                    anxietyLevel > 50 -> Color.Yellow
                    else -> Color.Green
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "$anxietyLevel%",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}


fun generateMockHeartbeatData(): List<Int> {
    // Base heartbeat range for a resting adult: 60-100 BPM
    val baseLineBpm = 60..100

    // Generate mock data with gradual changes and occasional peaks
    val data = mutableListOf<Int>()

    var lastBpm = baseLineBpm.random() // Starting point

    for (i in 1..19) { // Reduced to 20 measurements
        when {
            // Simulate a sudden spike or drop every 5 measurements
            i % 5 == 0 -> {
                val spikeDrop = listOf(-10, 10).random() // Randomly choose to spike or drop
                lastBpm = (lastBpm + spikeDrop).coerceIn(baseLineBpm)
            }
            // Gradual fluctuation for other measurements
            else -> {
                // Allow small fluctuation of +/- 3 BPM from the last measurement
                lastBpm = (lastBpm + Random.nextInt(-3, 4)).coerceIn(baseLineBpm)
            }
        }
        data.add(lastBpm)
    }

    return data
}



@Composable
fun DailySummaryGraph(dailySummary: List<Float>) {
    Card(modifier = Modifier.padding(16.dp), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "סיכום חרדה יומי", style = MaterialTheme.typography.h6)
            LazyRow { // Using LazyRow for horizontal scrolling if there are many days
                items(dailySummary.size) { index ->
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(50.dp)
                            .height(200.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(dailySummary[index] * 2.dp) // Simulating graph bar height
                                .background(Color.Blue, shape = RoundedCornerShape(4.dp))
                        )
                        Text(
                            text = "${dailySummary[index]}",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(4.dp)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TipsAndAdviceSection(tips: List<String>) {
    Card(modifier = Modifier.padding(16.dp), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "טיפים יומיים", style = MaterialTheme.typography.h6)
            tips.forEach { tip ->
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Tip",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = tip, style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}

@Composable
fun FeelingHeader() {
    val currentTime = LocalTime.now()
    val greeting = when (currentTime.hour) {
        in 0..11 -> "בוקר טוב"
        in 12..16 -> "אחר צהריים טובים"
        in 17..23 -> "ערב טוב"
        else -> "שלום"
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "$greeting, איך אתה מרגיש היום?",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "בדוק את רמת החרדה עכשיו ועיין בטיפים של היום",
            style = MaterialTheme.typography.body1,
            color = Color.Gray
        )
    }
}

@Composable
fun LastHeartbeatCard(heartbeatData: List<Int>) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "מדידה אחרונה: ${heartbeatData.last()} BPM", // Example, replace with real data
                style = MaterialTheme.typography.h6
            )
            val hours = List(24) { i -> "${i}:00" } // or format as needed
            val mockHeartbeatData = generateMockHeartbeatData()  // Assuming this generates 24 data points

            PulseLineChart(dataPoints = mockHeartbeatData, hours = hours)
        }
    }
}

@Composable
fun PulseLineChart(dataPoints: List<Int>, hours: List<String>, lineColor: Color = MaterialTheme.colors.primary) {
    val strokeWidth = with(LocalDensity.current) { 2.dp.toPx() }
    val pathEffect = PathEffect.cornerPathEffect(50f) // Smooth corners
    var textSize = with(LocalDensity.current) { 12.sp.toPx() }
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = with(LocalDensity.current) { 20.sp.toPx() }
        color = android.graphics.Color.BLACK
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .background(Color.LightGray.copy(alpha = 0.3f))) {

        // Constants for chart dimensions
        val maxDataPoint = 100  // Maximum for y-axis
        val minDataPoint = 0    // Minimum for y-axis
        val yLabels = List(11) { i -> i * 10 }  // Y-axis labels from 0 to 100
        val xLabelIndices = if (hours.size > 10) hours.indices.step(hours.size / 10) else hours.indices  // Reduce the number of x-labels if too many

        // Calculate scales for data points
        val heightScale = size.height / (maxDataPoint - minDataPoint).toFloat()
        val widthScale = size.width / (hours.size - 1).toFloat()

        // Draw Y-axis labels
        yLabels.forEach { value ->
            val y = size.height - (value - minDataPoint) * heightScale
            drawContext.canvas.nativeCanvas.drawText("$value", 0f, y, textPaint)
        }

        // Draw X-axis labels (reduced frequency for readability)
        xLabelIndices.forEach { index ->
            val hour = hours[index]
            val x = index * widthScale
            drawContext.canvas.nativeCanvas.drawText(hour, x, size.height - 5.dp.toPx(), textPaint)
        }

        // Drawing the line graph
        val graphPath = Path().apply {
            moveTo(0f, size.height - (dataPoints[0] - minDataPoint) * heightScale) // Start point
            dataPoints.forEachIndexed { index, dataPoint ->
                val x = index * widthScale
                val y = size.height - (dataPoint - minDataPoint) * heightScale
                lineTo(x, y)
            }
        }
        drawPath(graphPath, lineColor, style = Stroke(width = strokeWidth, pathEffect = pathEffect))

        // Draw circles at data points
        dataPoints.forEachIndexed { index, dataPoint ->
            val x = index * widthScale
            val y = size.height - (dataPoint - minDataPoint) * heightScale
            drawCircle(color = lineColor, radius = strokeWidth * 1.5f, center = Offset(x, y))
        }
    }
}