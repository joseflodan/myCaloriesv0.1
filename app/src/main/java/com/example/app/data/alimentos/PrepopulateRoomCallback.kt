package com.example.app.data.alimentos

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.app.R
import com.example.app.data.UserDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateUsers(context)
        }
    }

    suspend fun prePopulateUsers(context: Context) {
        try {
            val alimentDao = UserDataBase.getDataBase(context).alimentDao()

            val userList: JSONArray =
                context.resources.openRawResource(R.raw.alimentos).bufferedReader().use {
                    JSONArray(it.readText())
                }

            userList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val userObj = list.getJSONObject(index)
                    alimentDao.insertAliment(
                        Aliment(
                            id =userObj.getInt("id"),
                            nombre = userObj.getString("nombre"),
                            Kcal = userObj.getInt("Kcal"),
                            porcion = userObj.getString("porcion")
                        )
                    )

                }
                Log.e("User App", "successfully pre-populated users into database")
            }
        } catch (exception: Exception) {
            Log.e(
                "User App",
                exception.localizedMessage ?: "failed to pre-populate users into database"
            )
        }
    }
}