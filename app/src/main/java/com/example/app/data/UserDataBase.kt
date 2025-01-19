package com.example.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.data.product.Product
import com.example.app.data.product.ProductDao
import com.example.app.data.user.User
import com.example.app.data.user.userDao


@Database(entities = [User::class, Product::class], version = 1, exportSchema = true)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDao(): userDao

    abstract fun productDao(): ProductDao

    companion object{
        @Volatile
        private var Instance: UserDataBase? = null

        fun getDataBase(context: Context): UserDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, UserDataBase::class.java, "database")
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}