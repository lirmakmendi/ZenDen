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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                R.drawable.deep_breathing
            )
        }
//        item {
//            RelaxationCard(
//                "Progressive Muscle Relaxation",
//                "Gradually tighten and relax each muscle group to reduce physical stress.",
//                R.drawable.muscle_relaxation
//            )
//        }
        item {
            RelaxationCard(
                "דמיון מודרך",
                "השתמש בדמיון שלך כדי לצאת למסע ויזואלי למקום או למצב שליו ומרגיע.",
                R.drawable.guided_imagery
            )
        }
        item {
            RelaxationCard(
                "מדיטציית מיינדפולנס",
                "שימו לב לרגע הנוכחי.",
                R.drawable.meditation
            )
        }
        // Add more techniques here
    }
}

@Composable
fun RelaxationCard(title: String, description: String, imageRes: Int) {
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
                onClick = { /* Implement navigation to detailed content */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.PlayCircle, contentDescription = "Start")
                Spacer(Modifier.width(8.dp))
                Text("התחל תרגול")
            }
        }
    }
}