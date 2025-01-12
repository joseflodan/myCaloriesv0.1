package com.example.app.data

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDao(): userDao

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