package com.example.app.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.app.MyApp

object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            OffLineUserViewModel(myApp().container.userRepository)
        }
        initializer {
            OffLineProductViewModel(myApp().container.productRepository)
        }
    }

}

fun CreationExtras.myApp() : MyApp = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY ] as MyApp)