package com.example.app.data.asistente

import kotlinx.serialization.Serializable
import java.util.StringTokenizer

@Serializable
data class Asistente (
    val id: String = "",
    val name: String = ""
)