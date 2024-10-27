package com.example.app

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.Scanner

@Composable
fun MyCaloriesApp(
    navController: NavHostController = rememberNavController()
){
    Scaffold (
        topBar = {
            MyCaloriesAppBar(
                canNavgateBack = true,
                navigateUp = {}
            )
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MyCaloriesScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = MyCaloriesScreen.Login.name) {
                Login(
                    nexScreen = {
                        navController.navigate(MyCaloriesScreen.MainMenu.name)
                    }
                )
            }

            composable(route = MyCaloriesScreen.MainMenu.name) {
                MainMenu()
            }


            composable(route = MyCaloriesScreen.MainMenu.name){
                MainMenu(
                    scanner = {
                        navController.navigate(MyCaloriesScreen.Scanner.name)
                    }
                )
            }

            composable(route = MyCaloriesScreen.Scanner.name) {
                Scanner()
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCaloriesAppBar(
    canNavgateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
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
            if(canNavgateBack){
                IconButton(onClick = navigateUp){
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                }

            }
            IconButton(onClick ={
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