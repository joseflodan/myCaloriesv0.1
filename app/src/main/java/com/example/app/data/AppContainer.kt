package com.example.app.data

import android.content.Context
import com.example.app.data.alimentos.AlimentRepository
import com.example.app.data.product.OffLineProductRepository
import com.example.app.data.product.ProductRepository
import com.example.app.data.registro.OfflineCalenRepository
import com.example.app.data.registro.CalenRepository
import com.example.app.data.user.OfflineUserRepository
import com.example.app.data.user.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val productRepository: ProductRepository
    val calendarRepository: CalenRepository
    val alimentRepository: AlimentRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val userRepository : UserRepository by lazy {
        OfflineUserRepository(UserDataBase.getDataBase(context).userDao())
    }

    override  val productRepository : ProductRepository by lazy {
        OffLineProductRepository(UserDataBase.getDataBase(context).productDao())
    }

    override val calendarRepository : CalenRepository by lazy {
        OfflineCalenRepository(UserDataBase.getDataBase(context).calenDao())
    }
    override val alimentRepository: AlimentRepository
        get() = TODO("Not yet implemented")
}