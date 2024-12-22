package com.example.app.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)

    fun getUsers(email: Int): Flow<User>

    fun getAllItems(): Flow<List<User>>
}