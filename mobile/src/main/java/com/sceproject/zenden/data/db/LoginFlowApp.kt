package com.sceproject.zenden.data.db

import android.app.Application
import com.google.firebase.FirebaseApp

class LoginFlowApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}