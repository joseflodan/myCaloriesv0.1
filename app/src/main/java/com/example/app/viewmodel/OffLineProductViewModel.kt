package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.data.product.Product
import com.example.app.data.product.ProductRepository
import com.example.app.model.FoodResult

class OffLineProductViewModel(private val productRepository: ProductRepository): ViewModel()  {
    suspend fun guardarProducto (foodResult: FoodResult, barcode: String){
        val product = Product(
            barcode = barcode,
            brands = foodResult.product?.brands ?: "",
            imageUrl = foodResult.product?.imageUrl ?: "",
            productName = foodResult.product?.productName ?: "",
            energyKcal = foodResult.product?.nutriments?.energyKcal?.toDouble() ?: 0.0
        )
        productRepository.insert(product)
    }

    suspend fun getProduct (codigoCapturado: String) : Product{
        return productRepository.getProduct(codigoCapturado)
    }
}