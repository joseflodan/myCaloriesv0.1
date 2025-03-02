package com.example.app.data.registro

import kotlinx.coroutines.flow.Flow

class OfflineCalenRepository (private val userDao: calenDao): calenRepository {
    override suspend fun insert(calen: Calen) {
        userDao.insert(calen)
    }

    override suspend fun update(calen: Calen) {
        userDao.update(calen)
    }

    override suspend fun delete(calen: Calen) {
        userDao.delete(calen)
    }

    override fun getUsers(email: String): Calen {
        return userDao.getUsers(email)
    }

    override fun getAllItems(): Flow<List<Calen>> {
       return userDao.getAllUsers()
    }
}