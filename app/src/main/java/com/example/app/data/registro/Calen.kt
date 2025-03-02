package com.example.app.data.registro

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendario")

data class Calen(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String = "",
    val fecha: String = "",
    val tiempo: String = "",
    val calorias: Double = 0.0,
    val producto: String = ""
)