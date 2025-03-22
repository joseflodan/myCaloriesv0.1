package com.example.app.data.alimentos

import kotlinx.coroutines.flow.Flow

class AlimentRepository (private val alimentDao: AlimentDao) {

    fun getAliments(): Flow<List<Aliment>> = alimentDao.getAliments()

}