package com.sceproject.zenden.utils

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sceproject.zenden.workers.DailyReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleDailyReminder(context: Context) {
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS) // Optional: Delay to start at a specific time
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "DailyReminderWork",
            androidx.work.ExistingPeriodicWorkPolicy.REPLACE, // Or KEEP, depending on your use case
            dailyWorkRequest
        )
    }

    private fun calculateInitialDelay(): Long {
        val calendar = Calendar.getInstance()

        // Set the desired time - for example, 8 AM
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // Get the desired time in milliseconds
        val desiredTimeMillis = calendar.timeInMillis

        val currentTimeMillis = System.currentTimeMillis()

        return if (desiredTimeMillis > currentTimeMillis) {
            desiredTimeMillis - currentTimeMillis
        } else {
            desiredTimeMillis + TimeUnit.DAYS.toMillis(1) - currentTimeMillis
        }
    }
}
