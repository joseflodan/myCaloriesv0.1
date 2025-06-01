package com.example.app.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app.R
import com.example.app.data.alimentos.AlimentAI
import com.example.app.data.alimentos.AlimentRepository
import kotlinx.serialization.json.Json
import org.json.JSONObject

class OffLineAlimentViewModel (private val alimentRepository: AlimentRepository): ViewModel() {

    var listadeAlimentos: List<AlimentAI> by mutableStateOf(listOf())

    fun getProcesOpenIaResponse(context: Context, response: String){
        val resultado = JSONObject(response)
        val choices0 = resultado.getJSONArray("choices").get(0) as JSONObject
        val  content= choices0.getJSONObject("message").getString("content")

        listadeAlimentos = try{
            Json.decodeFromString<List<AlimentAI>>(content.trimIndent())
        }catch(e: Exception) {
            listOf(
                AlimentAI(
                    nombre = context.getString(R.string.no_data_alimentos),
                    Kcal = 0.0,
                    Porcion = "",
                    seleccionado = false
                )
            )
        }
    }

    fun updateSelectedAliments(alimento: AlimentAI) {
        listadeAlimentos = listadeAlimentos.map {
            if (it.nombre == alimento.nombre) {
                it.copy(seleccionado = !it.seleccionado)
            } else {
                it
            }
        }
    }
}