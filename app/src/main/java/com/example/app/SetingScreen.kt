package com.example.app

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.credential.BearerTokenCredential
import com.openai.models.ChatModel
import com.openai.models.chat.completions.ChatCompletionCreateParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SetingScreen(
    modifier: Modifier = Modifier,
    cerrarSesion: () -> Unit = {}

){
    val context = LocalContext.current

    val titulo = stringResource(R.string.titulo_screen1)
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(color = 0xFFd5bdaf),
                        Color(color = 0xFFedede9)
                    )
                )
            )
            .padding(5.dp)
            .fillMaxSize()
    ) {
        Text(
            text = titulo,
            fontSize = 35.sp,
            fontFamily = FontFamily.Serif ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
         OutlinedButton(
             onClick = {
                 callChatGPT()
             },
             modifier = Modifier
                 .padding(10.dp)
                 .fillMaxWidth(),
             colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
         ) {
            Text(text= "TEMA", color = Color.Black,fontSize = 20.sp)
         }
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ) {
            Text(text= "AYUDA", color = Color.Black,fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = {
                borrarDatos(context)
                cerrarSesion.invoke()
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ) {
            Text(text= "CERRAR SESION", color = Color.Black,fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ) {
            Text(text= "PROFECIONALES", color = Color.Black,fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ) {
            Text(text= "CAMBIAR DE CUENTA", color = Color.Black,fontSize = 20.sp)
        }
    }
}

private fun callChatGPT(){
    val client = OpenAIOkHttpClient.builder().apply {
        val openAIKey = BuildConfig.OPENAI_API_KEY
        credential(BearerTokenCredential.create(openAIKey))
    }.build()

    val params = ChatCompletionCreateParams.builder()
        .addUserMessage("Say this is a test")
        .model(ChatModel.GPT_4O)
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        val chatCompletion = client.chat().completions().create(params)
        withContext(Dispatchers.Main) {
            Log.d("Prueba",chatCompletion._choices().toString())
        }
    }
}

private fun borrarDatos (context: Context){
    val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
    with(sharedPref.edit()){
        remove("email")
        remove("hilo")
        apply()
    }
}

@Preview (showBackground = true)
@Composable
fun SetingPreview (){
    SetingScreen()

}
