package com.example.app

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun MyCaloriesApp(
    navController: NavHostController = rememberNavController()
){
    var backButton by remember { mutableStateOf(value = false) }
    Scaffold (
        topBar = {
            MyCaloriesAppBar(
                canNavgateBack = backButton,
                navigateUp = { navController.navigateUp()},
                settings = {
                    navController.navigate(MyCaloriesScreen.SetingScreen.name)
                }
            )
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MyCaloriesScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = MyCaloriesScreen.Login.name) {
                backButton = false
                Login(
                    nexScreen = {
                        navController.navigate(MyCaloriesScreen.MainMenu.name){
                            popUpTo(0)
                        }
                    }
                )
            }
            composable(route = MyCaloriesScreen.MainMenu.name){
                backButton = false
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
                IMCscreen()
            }
            composable(route = MyCaloriesScreen.Calendario.name) {
                backButton = true
                Calendario()
            }
            composable(route = MyCaloriesScreen.alimentos.name) {
                backButton = true
                alimentos()
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
            Text(stringResource(R.string.app_name))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFa07054)
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavgateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
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
                    contentDescription = ""
                )
            }
        }
    )
}