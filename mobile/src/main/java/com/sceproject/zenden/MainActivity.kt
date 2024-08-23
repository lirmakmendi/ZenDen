package com.sceproject.zenden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sceproject.zenden.app.MessageHandler
import com.sceproject.zenden.app.ZenDen
import com.sceproject.zenden.utils.NotificationScheduler

class MainActivity : ComponentActivity() {

    private lateinit var messageHandler: MessageHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenDen()
        }
        messageHandler = MessageHandler(this)
        NotificationScheduler.scheduleDailyReminder(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        messageHandler.onDestroy()
    }
}

