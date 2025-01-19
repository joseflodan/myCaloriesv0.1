package com.example.app.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.model.FoodResult
import com.example.app.model.Nutriments
import com.example.app.model.Product
import com.example.app.network.FoodApi
import kotlinx.coroutines.launch

class FoodApiViewModel: ViewModel() {
    var respuesta: FoodResult by mutableStateOf(FoodResult())


    suspend fun getFoodProduct(id: String){
            try{
                respuesta = FoodApi.retrofitService.getProduct(id)
            }catch (e: Exception) {
                Log.d("Prueva", "Producto no Encontrado")
                respuesta = FoodResult()
                respuesta.product = Product("no encontrado", "no encontrado","no encontrado", Nutriments(0.0))
            }
    }
}