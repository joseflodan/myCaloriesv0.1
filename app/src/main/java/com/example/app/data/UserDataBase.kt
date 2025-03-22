package com.example.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.data.alimentos.Aliment
import com.example.app.data.alimentos.AlimentDao
import com.example.app.data.alimentos.PrepopulateRoomCallback
import com.example.app.data.product.Product
import com.example.app.data.product.ProductDao
import com.example.app.data.registro.Calen
import com.example.app.data.registro.calenDao
import com.example.app.data.user.User
import com.example.app.data.user.userDao


@Database(entities = [User::class, Product::class, Calen::class, Aliment::class], version = 1, exportSchema = true)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDao(): userDao
    abstract fun productDao(): ProductDao
    abstract fun calenDao(): calenDao
    abstract fun alimentDao(): AlimentDao

    companion object{
        @Volatile
        private var Instance: UserDataBase? = null

        fun getDataBase(context: Context): UserDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, UserDataBase::class.java, "database")
                    .allowMainThreadQueries()
                    .addCallback(PrepopulateRoomCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }
    }

}