package com.example.app

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.screens.ThemeSelectorScreen
import com.example.app.ui.theme.AppTheme
import com.example.app.utils.PrefsHelper

@Composable
fun MyCaloriesApp(
    navController: NavHostController = rememberNavController()
){
    var backButton by remember { mutableStateOf(value = false) }
    var topbar by remember {mutableStateOf(value = false)}
    Scaffold (
        topBar = {
            if(topbar) {
                MyCaloriesAppBar(
                    canNavgateBack = backButton,
                    navigateUp = { navController.navigateUp() },
                    settings = {
                        navController.navigate(MyCaloriesScreen.SetingScreen.name)
                    }
                )
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MyCaloriesScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = MyCaloriesScreen.Login.name) {
                backButton = false
                topbar = false
                LoginScreen(
                    nextScreen = {
                        navController.navigate(MyCaloriesScreen.MainMenu.name){
                            popUpTo(0)
                        }
                    },
                    primerInicioSesion = {
                        navController.navigate(MyCaloriesScreen.IMCscreen.name){
                            popUpTo(0)
                        }
                    }
                )
            }
            composable(route = MyCaloriesScreen.MainMenu.name){
                backButton = false
                topbar = true
                MainMenu(
                    scanner = {
                        navController.navigate(MyCaloriesScreen.Scanner.name)
                    },
                    ayunoInter = {
                        navController.navigate(MyCaloriesScreen.AyunoScreen.name)
                    },
                    contador = {
                        navController.navigate(MyCaloriesScreen.ContadorScreen.name)
                    },
                    imc = {
                        navController.navigate(MyCaloriesScreen.IMCscreen.name)
                    },
                    calen = {
                        navController.navigate(MyCaloriesScreen.Calendario.name)
                    },
                    alim={
                        navController.navigate(MyCaloriesScreen.alimentos.name)
                    },
                    chat={
                        navController.navigate(MyCaloriesScreen.charbot.name)
                    }
                )
            }
            composable(route = MyCaloriesScreen.SetingScreen.name) {
                backButton = true
                SetingScreen(
                    cerrarSesion = {
                        navController.navigate(MyCaloriesScreen.Login.name){
                            popUpTo(0)
                        }
                    },
                    tema = {
                        navController.navigate(MyCaloriesScreen.ThemesSelectionScreen.name)
                    }
                )
            }
            composable(route = MyCaloriesScreen.ThemesSelectionScreen.name) {
                val context = LocalContext.current
                backButton = true
                ThemeSelectorScreen(
                    onBack = { navController.navigateUp() },
                    onThemeSelected = {theme ->
                        ThemeState.currentTheme = theme.theme
                        PrefsHelper.saveTheme(context, theme.theme)
                        navController.navigate(MyCaloriesScreen.MainMenu.name){
                            popUpTo(0)
                        }
                    }
                )
            }
            composable(route = MyCaloriesScreen.Scanner.name) {
                backButton = true
                Scanner()
            }
            composable(route = MyCaloriesScreen.AyunoScreen.name) {
                backButton = true
                AyunoScreen()
            }
            composable(route = MyCaloriesScreen.ContadorScreen.name) {
                backButton = true
                ContadorScreen()
            }
            composable(route = MyCaloriesScreen.IMCscreen.name) {
                backButton = true
                IMCscreen(
                    menuPrincipal = {
                        navController.navigate(MyCaloriesScreen.MainMenu.name){
                            popUpTo(0)
                        }
                    }
                )
            }
            composable(route = MyCaloriesScreen.Calendario.name) {
                backButton = true
                Calendario()
            }
            composable(route = MyCaloriesScreen.alimentos.name) {
                backButton = true
                alimentos()
            }
            composable(route = MyCaloriesScreen.charbot.name) {
                backButton = true
                topbar = false
                chatbot(
                    navigateUp={navController.navigateUp()}

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCaloriesAppBar(
    canNavgateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    settings: () -> Unit = {},
    cierreSecion: () -> Unit = {}
){
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name), fontSize = 30.sp, color = Color.White)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6a815b)
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavgateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp),
                        contentDescription = ""
                    )
                }

            }
        },
        actions = {
            IconButton(onClick ={
                settings.invoke()
            }
            ){
                Icon(
                    imageVector = Icons.Filled.Settings,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp),
                    contentDescription = ""
                )
            }
        }
    )
}