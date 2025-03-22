package com.example.app.data.alimentos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlimentDao {
    @Query("SELECT * FROM alimentos")
    fun getAliments(): List<Aliment>

    @Insert
    suspend fun insertAliment(aliment: Aliment)

    @Insert
    suspend fun insertAll(alimentos: List<Aliment>)
}