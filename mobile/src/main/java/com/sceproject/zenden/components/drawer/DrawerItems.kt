package com.sceproject.zenden.components.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Watch
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItems(
    val title: String,
    val description: String,
    val itemId: String,
    val icon: ImageVector
) {
    companion object {
        val drawerItemsList = listOf(
            DrawerItems(
                title = "מסך הבית",
                icon = Icons.Default.Dashboard,
                description = "Dashboard",
                itemId = "dashboardScreen"
            ),
            DrawerItems(
                title = "מענה על שאלון",
                icon = Icons.Default.Watch,
                description = "Answer PDSS",
                itemId = "AnswerPDSSScreen"
            ),
            DrawerItems(
                title = "בדיקת התקף חרדה",
                icon = Icons.Default.Check,
                description = "Check Panic Attack",
                itemId = "CheckPanicAttackScreen"
            ),
            DrawerItems(
                title = "היסטוריית חרדה",
                icon = Icons.Default.ShowChart,
                description = "View Anxiety Trends",
                itemId = "anxietyTrendsScreen"
            ),
            DrawerItems(
                title = "טכניקות הרגעה",
                icon = Icons.Default.SelfImprovement,
                description = "Learn Relaxation Techniques",
                itemId = "relaxationTechniquesScreen"
            ),
            DrawerItems(
                title = "מידע על התקפי חרדה",
                icon = Icons.Default.MedicalInformation,
                description = "Panic Attack Information",
                itemId = "panicAttackInfoScreen"
            ),
            DrawerItems(
                title = "הפרופיל שלי",
                icon = Icons.Default.Person,
                description = "My Profile",
                itemId = "myProfileScreen"
            ),

            )
    }
}