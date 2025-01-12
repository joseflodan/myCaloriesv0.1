package com.example.app.data

import kotlinx.coroutines.flow.Flow

class OfflineUserRepository (private val userDao: userDao): UserRepository {
    override suspend fun insert(user: User) {
        userDao.insert(user)
    }

    override suspend fun update(user: User) {
        userDao.update(user)
    }

    override suspend fun delete(user: User) {
        userDao.delete(user)
    }

    override fun getUsers(email: String): User {
        return userDao.getUsers(email)
    }

    override fun getAllItems(): Flow<List<User>> {
       return userDao.getAllUsers()
    }
}