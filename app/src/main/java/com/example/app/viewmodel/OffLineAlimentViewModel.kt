package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.data.alimentos.Aliment
import com.example.app.data.alimentos.AlimentRepository

class OffLineAlimentViewModel (private val alimentRepository: AlimentRepository): ViewModel() {
    fun getAliments(): List<Aliment> {
        return alimentRepository.getAliments()
    }
}