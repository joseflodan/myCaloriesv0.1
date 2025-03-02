package com.example.app.data.registro

interface CalenRepository {
    suspend fun insert(calen: Calen)

    suspend fun update(calen: Calen)

    suspend fun delete(calen: Calen)

    fun getRegistros(email: String, fecha: String): List<Calen>
}