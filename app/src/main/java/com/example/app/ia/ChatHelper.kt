package com.example.app.ia

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.app.BuildConfig
import com.example.app.data.asistente.Asistente
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ChatHelper {
    suspend fun cargarImagen(context: Context, capturedImageUri: Uri): String =
        withContext(Dispatchers.IO) {
            val imputStream = context.contentResolver.openInputStream(capturedImageUri)
            val bitmap = BitmapFactory.decodeStream(imputStream)
            imputStream?.close()

            if (bitmap != null) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
                val data = baos.toByteArray()

                val storageRef = Firebase.storage.reference
                val ImagesRef = storageRef.child("images/Food.jpg")

                var uploadTask = ImagesRef.putBytes(data)
                val result = uploadTask.await()
                val downloadUrl = result.storage.downloadUrl.await()
                consultarAlimentosEnFoto(downloadUrl.toString())
            } else {
                ""
            }
        }

    suspend fun consultarAlimentosEnFoto(url: String): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val jsonMessage = """
        {
            "model": "gpt-4o",
            "messages": [
                {
                    "role":"system",
                    "content": "si la imagen no contiene alimentos debes responder unicamente \"no hay alimentos en la imagen\", si la imagen contiene alimentos entonces vas a responder un json con la siguiente estructura [{\"nombre\": \"alimento1\", \"Kcal\":\"100\",\"Porcion\":\"100g\"},{\"nombre\": \"alimento2\", \"Kcal\":\"100\",\"Porcion\":\"100g\"}] las Kcal se deben calcular en base a porciones de 100g, el tamaño de la porcion siempre sera fijo a 100g"
                },
                {
                    "role": "user",
                    "content": [
                        {
                            "type": "text",
                            "text": "¿Qué ves en esta imagen?"
                        },
                        {
                            "type": "image_url",
                            "image_url": {
                                "url": "$url"
                            }
                        }
                    ]
                }
            ]
        }
    """.trimIndent()

        val body = jsonMessage.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string() ?: throw Exception("Empty response body")
        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }

    suspend fun consultarAsistentes(): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()


        val request = Request.Builder()
            .url("https://api.openai.com/v1/assistants")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val resultado= JSONObject(response.body?.string().toString())
            val data = resultado.getJSONArray("data")
            val llavesDesconocidas = Json{ignoreUnknownKeys = true}
            val listaAsistentes  = llavesDesconocidas.decodeFromString<List<Asistente>>(data.toString())
            listaAsistentes.filter {
                it.name == "asistente_nutricion"
            }

            listaAsistentes.first().id
        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }

    suspend fun crearHilo(): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()


        val request = Request.Builder()
            .url("https://api.openai.com/v1/threads")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .post("".toRequestBody())
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val resultado= JSONObject(response.body?.string().toString())
            resultado.getString("id")
        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }

    suspend fun enviarMensaje(hiloId: String, mensaje: String): String = withContext(Dispatchers.IO){
        try{
            val runId = ejecutarChat(hiloId,mensaje)
            while (getStatus(hiloId, runId) != "completed"){
                Log.d("ChatHelper","Esperando respuesta")
                Thread.sleep(1000)
            }
            obtenerMensajes(hiloId)
        }catch (e: Exception){
            "Hilo Ejecutrandose"
        }

    }

    suspend fun crearMensaje(hiloId: String, mensaje: String): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val jsonMessage = """
    {
      "role": "user",
      "content": "$mensaje"
    }
    """.trimIndent()
        val body = jsonMessage.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/threads/$hiloId/messages")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val resultado= JSONObject(response.body?.string().toString())
            resultado.getString("id")
        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }

    suspend fun ejecutarChat(hiloId: String, mensaje: String): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val mensajeId = crearMensaje(hiloId, mensaje)
        val asistenteId = consultarAsistentes()
        val jsonMessage = """
    {
      "assistant_id": "$asistenteId"
    }
    """.trimIndent()
        val body = jsonMessage.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/threads/$hiloId/runs")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val resultado= JSONObject(response.body?.string().toString())
            resultado.getString("id")
        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }

    suspend fun getStatus(hiloId: String,runId: String): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/threads/$hiloId/runs/$runId")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val resultado= JSONObject(response.body?.string().toString())
            resultado.getString("status")
        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }

    suspend fun obtenerMensajes(hiloId : String): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.openai.com/v1/threads/$hiloId/messages")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val resultado= JSONObject(response.body?.string().toString())
            resultado.getJSONArray("data").toString()

        } else {
            throw Exception("HTTP error ${response.body?.string()}")
        }
    }
}



