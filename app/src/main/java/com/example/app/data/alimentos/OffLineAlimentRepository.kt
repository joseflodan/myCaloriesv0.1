package com.example.app.data.alimentos

class OffLineAlimentRepository(private val alimentDao: AlimentDao): AlimentRepository {
    override fun getAliments(): List<Aliment> {
        return alimentDao.getAliments()
    }
}