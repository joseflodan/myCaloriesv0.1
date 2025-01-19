package com.example.app.data.product

interface ProductRepository {
    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(product: Product)

    suspend fun getProduct(barcode: String): Product

}