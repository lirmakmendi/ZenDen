package com.sceproject.zenden.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sceproject.zenden.R
import com.sceproject.zenden.utils.NotificationUtils

class DailyReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Use the utility to send the notification
        NotificationUtils.sendNotification(applicationContext)
        return Result.success()
    }
}