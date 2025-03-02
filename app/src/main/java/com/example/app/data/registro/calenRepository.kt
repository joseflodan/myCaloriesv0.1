package com.example.app.data.registro

import kotlinx.coroutines.flow.Flow

interface calenRepository {
    suspend fun insert(calen: Calen)

    suspend fun update(calen: Calen)

    suspend fun delete(calen: Calen)

    fun getUsers(email: String): Calen

    fun getAllItems(): Flow<List<Calen>>
}