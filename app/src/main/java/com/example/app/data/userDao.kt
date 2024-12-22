package com.example.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface userDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUsers(email: Int): Flow<User>

    @Query("SELECT * from users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

}