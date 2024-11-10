package com.example.app.model

enum class CondicionesMedicas{
    diabetes,
    sobrepeso,
    saludable
}

data class User(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val condicion: CondicionesMedicas = CondicionesMedicas.saludable
)