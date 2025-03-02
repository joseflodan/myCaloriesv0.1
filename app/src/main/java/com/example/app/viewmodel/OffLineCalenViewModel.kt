package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.data.registro.Calen
import com.example.app.data.registro.CalenRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OffLineCalenViewModel(private val calenRepository: CalenRepository): ViewModel()  {
    suspend fun updateCalories(email: String, calorias: Double, producto: String) {
        val registro = Calen(
            email = email,
            fecha= getCalen(),
            tiempo= getTime(),
            calorias = calorias,
            producto = producto
        )
        calenRepository.insert(calen = registro)
    }
    private fun getCalen(): String {
        val calendar = Calendar.getInstance(Locale.ROOT)
        return SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(calendar.time)
    }

    private fun getTime(): String {
        val calendar = Calendar.getInstance(Locale.ROOT)
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
    }

    fun getTodayCalories(email: String): Double {
        val registros = calenRepository.getRegistros(email,getCalen())
        if (registros != null) {
            return registros.sumOf { it.calorias }
        }else{
            return 0.0
        }
    }

    fun getCaloriesByDay(email: String, fecha : String): Double {
        val registros = calenRepository.getRegistros(email, fecha )
        if (registros != null) {
            return registros.sumOf { it.calorias }
        }else{
            return 0.0
        }
    }
}