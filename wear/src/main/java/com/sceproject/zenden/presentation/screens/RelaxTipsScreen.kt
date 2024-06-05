package com.sceproject.zenden.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.sceproject.zenden.presentation.app.TopAppBarWithBackButton

@Composable
fun RelaxTipsScreen(onBack: () -> Unit) {
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
                Text(
                    "1. קחו חמש נשימות עמוקות - שאפו עמוק, החזיקו לרגע ונשפו לאט. התמקדו אך ורק בנשימה.",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "2. יישם את טכניקת 5-4-3-2-1 - זהה חמישה דברים שאתה יכול לראות, ארבעה דברים שאתה יכול לגעת בהם, שלושה דברים שאתה יכול לשמוע, שני דברים שאתה יכול להריח, ודבר אחד שאתה יכול לטעום.",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "3. מתיחה - בצע סדרה מהירה של מתיחות צוואר וכתפיים כדי לשחרר מתח.",
                    style = MaterialTheme.typography.body1
                )
                // Add more tips with Spacers as needed for readability
            }
        }
    }
}
