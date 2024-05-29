package com.sceproject.zenden.components.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sceproject.zenden.screens.admin.Statistics

@Composable
fun AdminContent(paddingValues: PaddingValues, statistics: Statistics) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "מסך אדמין",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            StatisticsCard(
                title = "סך כל המשתמשים",
                value = statistics.totalUsers.toString(),
                icon = Icons.Default.Person,
                iconColor = Color.Blue
            )

            StatisticsCard(
                title = "משתמשים פעילים",
                value = statistics.activeUsers.toString(),
                icon = Icons.Default.CheckCircle,
                iconColor = Color.Green
            )

            StatisticsCard(
                title = "משתמשים לא פעילים",
                value = statistics.inactiveUsers.toString(),
                icon = Icons.Default.Block,
                iconColor = Color.Red
            )

            StatisticsCard(
                title = "הצטרפו החודש",
                value = statistics.newUsersThisMonth.toString(),
                icon = Icons.Default.PersonAdd,
                iconColor = Color.Cyan
            )

        }
    }
}

@Composable
fun StatisticsCard(title: String, value: String, icon: ImageVector, iconColor: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(text = value, style = MaterialTheme.typography.body1)
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
