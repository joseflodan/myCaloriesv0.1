package com.example.app.data.registro

class OfflineCalenRepository (private val calenDAO: calenDao): CalenRepository {
    override suspend fun insert(calen: Calen) {
        calenDAO.insert(calen)
    }

    override suspend fun update(calen: Calen) {
        calenDAO.update(calen)
    }

    override suspend fun delete(calen: Calen) {
        calenDAO.delete(calen)
    }

    override fun getRegistros(email: String, fecha: String): List<Calen> {
        return calenDAO.getRegistros(email,fecha)
    }
}