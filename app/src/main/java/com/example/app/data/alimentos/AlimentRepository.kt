package com.example.app.data.alimentos

class AlimentRepository (private val alimentDao: AlimentDao) {

    fun getAliments(): List<Aliment> = alimentDao.getAliments()

}