package com.example.app

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp: Application() {
    companion object{  var USER_ID = ""}

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}