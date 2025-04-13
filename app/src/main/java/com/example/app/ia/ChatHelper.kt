package com.example.app.ia

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import com.example.app.BuildConfig
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.credential.BearerTokenCredential
import com.openai.models.ChatModel
import com.openai.models.chat.completions.ChatCompletionCreateParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ChatHelper {
    fun consultarAlimentosEnFoto(context: Context, capturedImageUri: Uri) {
        val client = OkHttpClient()

        val imageBytes = context.contentResolver.openInputStream(capturedImageUri)?.readBytes()
            ?: throw IOException("No se pudo leer el URI")

        // Parte del archivo (la imagen)
        val imageRequestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val variable = MultipartBody.Part.createFormData("file", "image.jpg", imageRequestBody)

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
                                "url": "attachment://image.jpg"
                            }
                        }
                    ]
                }
            ]
        }
    """.trimIndent()

        val jsonPart = jsonMessage.toRequestBody("application/json".toMediaTypeOrNull())

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("payload_json", null, jsonPart) // mensaje JSON
            .addFormDataPart("file", "image.jpg", imageRequestBody) // imagen adjunta
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GPT", "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("GPT", "Respuesta: ${response.body?.string()}")
            }
        })
    }

    fun createRequestBodyFromUri(context: Context, uri: Uri, mimeType: String = "image/jpeg"): RequestBody {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: throw IOException("No se pudo leer el URI")
        return bytes.toRequestBody(mimeType.toMediaTypeOrNull())
    }

    fun createMultipartPartFromUri(context: Context, uri: Uri, fieldName: String = "file"): MultipartBody.Part {
        val fileName = "image.jpg" // o puedes intentar obtenerlo desde metadata
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val requestBody = createRequestBodyFromUri(context, uri, mimeType)

        return MultipartBody.Part.createFormData(fieldName, fileName, requestBody)
    }

    fun uriToBase64(context: Context, imageUri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val byteArray = inputStream?.readBytes()
            inputStream?.close()

            byteArray?.let {
                Base64.encodeToString(it, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getBitmapFromUri(context: Context, imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }
    }

    fun compressBitmapToByteArray(bitmap: Bitmap, quality: Int = 0): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

    fun encodeToBase64(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun compressAndEncodeImage(context: Context, imageUri: Uri): String {
        val bitmap = getBitmapFromUri(context, imageUri)
        val compressedBytes = compressBitmapToByteArray(bitmap)
        return encodeToBase64(compressedBytes)
    }
}