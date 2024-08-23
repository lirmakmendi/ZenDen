package com.sceproject.zenden.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.ListenableWorker
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DailyReminderWorkerTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
    }

    @Test
    fun testDailyReminderWorker() {
        // Create a test instance of the worker
        val worker = TestListenableWorkerBuilder<DailyReminderWorker>(context)
            .build()

        // Run the worker synchronously
        val result = worker.startWork().get()

        // Assert that the worker's result is success
        assertEquals(ListenableWorker.Result.success(), result)
    }
}
