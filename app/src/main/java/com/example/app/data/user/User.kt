package com.example.app.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")

data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val condicion: String = "",
    var calorias: Double = 0.0,
    var imc: Double = 0.0
)