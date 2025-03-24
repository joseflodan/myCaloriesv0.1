package com.example.app.data.alimentos

import com.example.app.data.product.Product

interface AlimentRepository {
    fun getAliments():  List<Aliment>
}