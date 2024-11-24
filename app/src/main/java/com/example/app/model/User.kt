package com.example.app.model

data class User(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val condicion: String = "",
    val calorias: Float = 0f
)