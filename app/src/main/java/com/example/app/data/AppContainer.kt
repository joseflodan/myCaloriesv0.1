package com.example.app.data

import android.content.Context
import com.example.app.data.product.OffLineProductRepository
import com.example.app.data.product.ProductRepository
import com.example.app.data.user.OfflineUserRepository
import com.example.app.data.user.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val productRepository: ProductRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val userRepository : UserRepository by lazy {
        OfflineUserRepository(UserDataBase.getDataBase(context).userDao())
    }

    override  val productRepository : ProductRepository by lazy {
        OffLineProductRepository(UserDataBase.getDataBase(context).productDao())
    }
}