package com.example.app

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ia.ChatHelper
import com.example.app.utils.PrefsHelper
import com.example.app.utils.Roles
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.ChatViewModel
import com.example.app.viewmodel.OffLineUserViewModel
import kotlinx.coroutines.launch

@Composable
fun chatbot(
    ChatViewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory),
    UserViewModel: OffLineUserViewModel = viewModel(factory = AppViewModelProvider.Factory),

    ) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("Hola") }
    var ejecutandoHilo by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var hiloid = PrefsHelper.getHilo(context)
    LaunchedEffect(hiloid) {
        if (hiloid.isNullOrEmpty()) {
            coroutineScope.launch {
                hiloid = ChatHelper().crearHilo()
                PrefsHelper.saveHilo(context, hiloid.toString())
                val mensjes = enviarMensajeIniciar(context = context,UserViewModel, hiloid.toString())
                ChatViewModel.procesarMensajes(mensjes)
            }
        } else{
            coroutineScope.launch{
                val mensjes = ChatHelper().obtenerMensajes(hiloid.toString())
                ChatViewModel.procesarMensajes(mensjes)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(color = 0xFFd5bdaf),
                        Color(color = 0xFFedede9)
                    )
                )
            )
            .padding(horizontal = 10.dp, vertical = 20.dp)
            .fillMaxSize()
    ){
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(ChatViewModel.listadeMensajes){ mensaje ->
                if (mensaje.role == Roles.user) {
                    Text(mensaje.content[0].text?.value.toString(), textAlign = TextAlign.Start)
                }else if (mensaje.role == Roles.assistant) {
                    Text(mensaje.content[0].text?.value.toString(), textAlign = TextAlign.End)
                }
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                value = message,
                onValueChange = { message = it },
            )
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (!ejecutandoHilo){
                            ejecutandoHilo = true
                            val data = ChatHelper().enviarMensaje(hiloid.toString(),message)
                            message = ""
                            ChatViewModel.procesarMensajes(data)
                        }else {
                            Toast.makeText(context, "Espere un momento", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = " ",
                    modifier = Modifier.scale(1.5f)
                )
            }
        }
    }
}

private suspend fun enviarMensajeIniciar(
    context: Context,
    viewModel: OffLineUserViewModel,
    hiloid: String
):String{
    val email = recuperarEMAIL(context)
    val user = viewModel.getUser(email.toString())
    val mensajeInicial = "Hola, soy ${user.name}, tengo ${user.edad} años, mido ${user.altura} cm y peso ${user.peso} kg" + "" +
            "Mi imc es ${user.imc}, mi tmb es ${user.tmb} y mi frecuencia de ejercicio es ${user.condicion} " +
            " soy ${if (user.sexo) "hombre" else "mujer"}"
    return ChatHelper().enviarMensaje(hiloid,mensajeInicial)
}

private fun recuperarEMAIL (context: Context): String?{
    val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
    return sharedPref.getString("email","")
}

@Preview(showBackground = true)
@Composable
fun chatbotPreview(){
    chatbot()
}