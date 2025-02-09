package com.example.app

import android.app.Application
import com.example.app.data.AppContainer
import com.example.app.data.AppDataContainer
import com.google.firebase.FirebaseApp

class MyApp: Application() {
    companion object{
        val PREFERENCIAS = "savedata"
        val EJERCICIO = arrayOf( "Poco o Nada","1 a 3 Dias","3 a 5 Dias", "6 a 7 Dias", "2 veces al Dia")
    }


    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = AppDataContainer(this)
    }
}