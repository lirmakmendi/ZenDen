package com.sceproject.zenden.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sceproject.zenden.R


@Composable
fun RelaxationContent(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                "בחר טכניקת הרגעה כדי להתחיל:",
                style = MaterialTheme.typography.h6
            )
        }
        item {
            RelaxationCard(
                "נשימה עמוקה",
                "תרגל נשימה עמוקה כדי לעזור להפחית מתח ולשפר את מצב הרוח שלך.",
                R.drawable.deep_breathing,
                "נשימה עמוקה היא טכניקה פשוטה ויעילה להפחתת מתח וחרדה. קח נשימה עמוקה דרך האף, החזק למשך כמה שניות, ונשוף באיטיות דרך הפה. חזור על התהליך מספר פעמים עד שתרגיש רגיעה.",
                listOf(
                    "https://www.youtube.com/embed/VUjiXcfKBn8?si=awmdVqFkiPGMAJ5Q",
                    "https://www.youtube.com/embed/enJyOTvEn4M?si=PVkCgW6ZEfEMatd5"
                )
            )
        }
        item {
            RelaxationCard(
                "דמיון מודרך",
                "השתמש בדמיון שלך כדי לצאת למסע ויזואלי למקום או למצב שליו ומרגיע.",
                R.drawable.guided_imagery,
                "דמיון מודרך הוא טכניקה שבה אתה מדמיין את עצמך במקום רגוע ושליו, כמו חוף ים או יער שקט. הטכניקה עוזרת להרגיע את הגוף והנפש ולהפחית מתח וחרדה.",
                listOf(
                    "https://www.youtube.com/embed/BbCZ131Zw8o?si=aICEA4-42aCAB0DL",
                    "https://www.youtube.com/embed/ez3GgRqhNvA?si=bhwp9fv8wO3b9QOX"
                )
            )
        }
        item {
            RelaxationCard(
                "מדיטציית מיינדפולנס",
                "שימו לב לרגע הנוכחי.",
                R.drawable.meditation,
                "מדיטציית מיינדפולנס היא טכניקה שבה מתמקדים בהווה ומתרכזים בתחושות הגוף, הנשימה והמחשבות ללא שיפוט. זה עוזר להפחית מתח ולהגביר את תחושת הרוגע והשלווה.",
                listOf(
                    "https://www.youtube.com/embed/DPjB-1OCUMA?si=6lpMaDigZLBHsHkz",
                    "https://www.youtube.com/embed/LJQOoAw0BjY?si=E0ghpmnfBqswuLLr"
                )
            )
        }
        // Add more techniques here
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RelaxationCard(title: String, description: String, imageRes: Int, detailedInfo: String, videoUrls: List<String>) {
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
                        Column {
                            Text(text = detailedInfo)
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow {
                                items(videoUrls) { videoUrl ->
                                    AndroidView(factory = { context ->
                                        WebView(context).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                            webViewClient = WebViewClient()
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true
                                            settings.loadWithOverviewMode = true
                                            settings.useWideViewPort = true
                                            settings.mediaPlaybackRequiresUserGesture = false
                                            loadUrl(videoUrl)
                                        }
                                    }, modifier = Modifier
                                        .width(300.dp)
                                        .height(200.dp)
                                        .padding(end = 16.dp))
                                }
                            }
                        }
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


@Composable
fun OfflineRelaxationContent(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                "בחר טכניקת הרגעה כדי להתחיל:",
                style = MaterialTheme.typography.h6
            )
        }
        item {
            RelaxationCard(
                "נשימה עמוקה",
                "תרגל נשימה עמוקה כדי לעזור להפחית מתח ולשפר את מצב הרוח שלך.",
                R.drawable.deep_breathing,
                "נשימה עמוקה היא טכניקה פשוטה ויעילה להפחתת מתח וחרדה. קח נשימה עמוקה דרך האף, החזק למשך כמה שניות, ונשוף באיטיות דרך הפה. חזור על התהליך מספר פעמים עד שתרגיש רגיעה.",
                listOf()  // Empty list for offline mode
            )
        }
        item {
            RelaxationCard(
                "דמיון מודרך",
                "השתמש בדמיון שלך כדי לצאת למסע ויזואלי למקום או למצב שליו ומרגיע.",
                R.drawable.guided_imagery,
                "דמיון מודרך הוא טכניקה שבה אתה מדמיין את עצמך במקום רגוע ושליו, כמו חוף ים או יער שקט. הטכניקה עוזרת להרגיע את הגוף והנפש ולהפחית מתח וחרדה.",
                listOf()  // Empty list for offline mode
            )
        }
        item {
            RelaxationCard(
                "מדיטציית מיינדפולנס",
                "שימו לב לרגע הנוכחי.",
                R.drawable.meditation,
                "מדיטציית מיינדפולנס היא טכניקה שבה מתמקדים בהווה ומתרכזים בתחושות הגוף, הנשימה והמחשבות ללא שיפוט. זה עוזר להפחית מתח ולהגביר את תחושת הרוגע והשלווה.",
                listOf()  // Empty list for offline mode
            )
        }
        item {
            Text(
                "לא ניתן להציג סרטונים במצב לא מקוון.",
                style = MaterialTheme.typography.body2,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        // Add more techniques here
    }
}
