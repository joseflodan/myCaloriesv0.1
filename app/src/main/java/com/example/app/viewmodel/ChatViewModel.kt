package com.example.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app.data.asistente.Mensaje
import kotlinx.serialization.json.Json

class ChatViewModel:ViewModel() {
    var listadeMensajes: List<Mensaje> by mutableStateOf(listOf())

    fun procesarMensajes(mensaje: String){

        listadeMensajes = try{
            val llavesDesconocidas = Json{ignoreUnknownKeys = true}
            llavesDesconocidas.decodeFromString<List<Mensaje>>(mensaje.trimIndent())
        }catch(e: Exception) {
            listOf()
        }
        listadeMensajes = listadeMensajes.sortedBy {
            it.createdAt ?:0
        }
    }
}