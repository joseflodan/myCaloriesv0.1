package com.example.app.utils

import android.content.Context
import com.example.app.MyApp

object PrefsHelper {
    private const val KEY_EMAIL = "email"
    private const val THREAD = "hilo"

    fun saveEmail(context: Context, email: String) {
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun getEmail(context: Context): String? =
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, "")

    fun saveHilo(context: Context, hiloId: String) {
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .edit()
            .putString(THREAD, hiloId)
            .apply()
    }

    fun getHilo(context: Context): String? =
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .getString(THREAD, "")


}