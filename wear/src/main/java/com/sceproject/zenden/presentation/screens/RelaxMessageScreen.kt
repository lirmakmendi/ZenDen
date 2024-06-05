package com.sceproject.zenden.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.sceproject.zenden.presentation.app.TopAppBarWithBackButton


@Composable
fun RelaxMessageScreen(onHelpRelax: () -> Unit, onGoToPhone: () -> Unit) {
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
