package com.sceproject.zenden.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sceproject.zenden.R
@Composable
fun PanicAttackInfoContent(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                "בחר נושא לקבלת מידע נוסף:",
                style = MaterialTheme.typography.h6
            )
        }
        item {
            InfoCard(
                "מה זה התקף חרדה?",
                "התקף חרדה הוא תגובה פתאומית ומפחידה המתרחשת לעיתים קרובות ללא סיבה ברורה.",
                R.drawable.panic_attack,
                "התקף חרדה הוא תחושת פחד אינטנסיבית ופתאומית המתרחשת לרוב ללא אזהרה מוקדמת. אנשים רבים חווים התקף חרדה לפחות פעם אחת במהלך חייהם. תסמיני התקף חרדה כוללים דופק מהיר, הזעה, רעידות, קוצר נשימה ועוד."
            )
        }
        item {
            InfoCard(
                "סימנים ותסמינים",
                "התקף חרדה עשוי לכלול תסמינים כמו קצב לב מהיר, נשימה מואצת, הזעה ועוד.",
                R.drawable.symptoms,
                "תסמיני התקף חרדה כוללים דופק מהיר, נשימה מואצת, הזעה, רעידות, תחושת חנק, כאבים בחזה ועוד. חשוב להכיר את התסמינים כדי לזהות התקף חרדה ולהתמודד איתו בצורה יעילה."
            )
        }
        item {
            InfoCard(
                "מתי לפנות לעזרה מקצועית",
                "חשוב לדעת מתי לפנות לעזרה מקצועית כדי לקבל תמיכה וטיפול מתאים.",
                R.drawable.professional_help,
                "אם אתה חווה התקפי חרדה תכופים או תסמינים חמורים המפריעים לחיי היום-יום שלך, חשוב לפנות לעזרה מקצועית. פסיכולוגים ופסיכיאטרים יכולים לעזור באבחון וטיפול בהתקפי חרדה באמצעות טיפול קוגניטיבי-התנהגותי ו/או תרופות."
            )
        }
        // Add more information cards as needed
    }
}

@Composable
fun InfoCard(title: String, description: String, imageRes: Int, detailedInfo: String) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.Info, contentDescription = "More Info")
                Spacer(Modifier.width(8.dp))
                Text("למידע נוסף")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(text = title)
                    },
                    text = {
                        Text(text = detailedInfo)
                    },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("סגור")
                        }
                    }
                )
            }
        }
    }
}
