package com.example.app.data.product

class OffLineProductRepository (private val productDao: ProductDao): ProductRepository {
    override suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    override suspend fun update(product: Product) {
        productDao.update(product)
    }

    override suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    override suspend fun getProduct(barcode: String): Product {
        return productDao.getproduct(barcode)
    }

}