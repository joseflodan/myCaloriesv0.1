package com.example.app.ia

import android.content.Context
import android.net.Uri
import com.example.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ChatHelper {
    suspend fun consultarAlimentosEnFoto(context: Context, capturedImageUri: Uri): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val url =
            "\"https://firebasestorage.googleapis.com/v0/b/mycalories-402b9.firebasestorage.app/o/bandeja.jpg?alt=media&token=cec63879-02d7-4338-bd7f-7d20cb801a49\""

        // Mensaje con imagen referenciada como attachment
        val jsonMessage = """
        {
            "model": "gpt-4o",
            "messages": [
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
                                "url": $url
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
            response.body?.toString() ?: throw Exception("Empty response body")
        } else {
            throw Exception("HTTP error ${response.code}: ${response.message}")
        }
    }
}