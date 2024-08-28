package com.sceproject.zenden.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.sceproject.zenden.R

object NotificationUtils {

    private const val CHANNEL_ID = "daily_reminder_channel"
    private const val CHANNEL_NAME = "Daily Reminders"
    private const val NOTIFICATION_ID = 1

    fun sendNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel if API level >= Oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
            .setContentTitle("Daily Anxiety Check")
            .setContentText("It's time to check your anxiety level and get today's tips!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
