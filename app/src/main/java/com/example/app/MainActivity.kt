package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MyCaloriesAppTheme
import com.example.app.utils.PrefsHelper

object ThemeState {
    var currentTheme by mutableStateOf(AppTheme.SAGE)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeState.currentTheme = PrefsHelper.getTheme(this)

        setContent {
            MyCaloriesAppTheme(selectedTheme = ThemeState.currentTheme) {
                MyCaloriesApp()
            }
        }
    }
}