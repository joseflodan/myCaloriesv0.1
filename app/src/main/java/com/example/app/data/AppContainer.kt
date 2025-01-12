package com.example.app.data

import android.content.Context

interface AppContainer {
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val userRepository : UserRepository by lazy {
        OfflineUserRepository(UserDataBase.getDataBase(context).userDao())
    }
}