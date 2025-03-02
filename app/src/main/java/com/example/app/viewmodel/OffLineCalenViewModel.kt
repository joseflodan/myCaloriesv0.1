package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.data.product.Product
import com.example.app.data.product.ProductRepository
import com.example.app.model.FoodResult

class OffLineCalenViewModel(private val calenRepository: ProductRepository): ViewModel()  {
    suspend fun guardarsuario(product: Product){
        calenRepository.insert(product)
    }
}