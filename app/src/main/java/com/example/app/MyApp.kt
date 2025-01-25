package com.example.app

import android.app.Application
import com.example.app.data.AppContainer
import com.example.app.data.AppDataContainer
import com.google.firebase.FirebaseApp

class MyApp: Application() {
    companion object{
        val PREFERENCIAS = "savedata"
    }


    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = AppDataContainer(this)
    }
}