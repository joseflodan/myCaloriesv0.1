package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.data.alimentos.AlimentRepository

class OffLineAlimentViewModel (private val alimentRepository: AlimentRepository): ViewModel() {
    val alimentos = alimentRepository.getAliments()
}