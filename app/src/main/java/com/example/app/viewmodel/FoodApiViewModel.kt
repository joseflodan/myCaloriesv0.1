package com.example.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.model.FoodResult
import com.example.app.network.FoodApi
import kotlinx.coroutines.launch

class FoodApiViewModel: ViewModel() {
    var respuesta: FoodResult by mutableStateOf(FoodResult())
        private set

    fun getFoodProduct(id: String){
        viewModelScope.launch {
            respuesta = FoodApi.retrofitService.getProduct(id)
        }
    }
}