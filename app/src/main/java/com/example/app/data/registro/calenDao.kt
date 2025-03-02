package com.example.app.data.registro

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface calenDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insert(calen: Calen)

    @Update
    suspend fun update(calen: Calen)

    @Delete
    suspend fun delete(calen: Calen)

    @Query("SELECT * FROM calendario WHERE email = :email")
    fun getUsers(email: String): Calen

    @Query("SELECT * from calendario ORDER BY name ASC")
    fun getAllUsers(): Flow<List<Calen>>

}