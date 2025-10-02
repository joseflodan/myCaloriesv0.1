package com.example.app.utils

import android.content.Context
import com.example.app.MyApp
import com.example.app.ui.theme.AppTheme
import androidx.core.content.edit

object PrefsHelper {
    private const val KEY_EMAIL = "email"
    private const val THREAD = "hilo"

    fun saveEmail(context: Context, email: String) {
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .edit() {
                putString(KEY_EMAIL, email)
            }
    }

    fun getEmail(context: Context): String? =
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, "")

    fun saveHilo(context: Context, hiloId: String) {
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .edit() {
                putString(THREAD, hiloId)
            }
    }

    fun getHilo(context: Context): String? =
        context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
            .getString(THREAD, "")


    fun saveTheme(context: Context, theme: AppTheme) {
        val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("APP_THEME", theme.name)
            apply()
        }
    }

    fun getTheme(context: Context): AppTheme {
        val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
        val themeName = sharedPref.getString("APP_THEME", AppTheme.SAGE.name)
        return AppTheme.valueOf(themeName ?: AppTheme.SAGE.name)
    }

}
