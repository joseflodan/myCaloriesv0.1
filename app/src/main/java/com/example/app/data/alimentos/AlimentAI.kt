package com.example.app.data.alimentos
import kotlinx.serialization.Serializable

@Serializable
data class AlimentAI (
    val nombre : String,
    val Kcal : Double,
    val Porcion : String,
    var seleccionado : Boolean = false
    )