package com.example.app

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ia.ChatHelper
import com.example.app.utils.PrefsHelper
import com.example.app.utils.Roles
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.ChatViewModel
import com.example.app.viewmodel.OffLineUserViewModel
import kotlinx.coroutines.launch

val lightGreen = Color(0xFFE6F0E6)
val darkGreen = Color(0xFFA5C4A5)
val DarkText = Color(0xFF232F27)
val backgroundColor = Color.White

@Composable
fun chatbot(
    ChatViewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory),
    UserViewModel: OffLineUserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var ejecutandoHilo by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var hiloid = PrefsHelper.getHilo(context)

    LaunchedEffect(hiloid) {
        if (hiloid.isNullOrEmpty()) {
            coroutineScope.launch {
                ejecutandoHilo = true
                hiloid = ChatHelper().crearHilo()
                PrefsHelper.saveHilo(context, hiloid.toString())
                val mensjes = enviarMensajeIniciar(context = context, UserViewModel, hiloid.toString())
                ChatViewModel.procesarMensajes(mensjes)
                ejecutandoHilo = false
            }
        } else {
            coroutineScope.launch {
                val mensjes = ChatHelper().obtenerMensajes(hiloid.toString())
                ChatViewModel.procesarMensajes(mensjes)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        ChatTopBar(onBackClicked = navigateUp)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            reverseLayout = true
        ) {
            items(ChatViewModel.listadeMensajes.reversed()) { mensaje ->
                val isUserMessage = mensaje.role == Roles.user
                MessageBubble(
                    messageText = mensaje.content[0].text?.value.toString(),
                    isUserMessage = isUserMessage
                )
            }
        }

        ChatInput(
            message = message,
            onMessageChange = { message = it },
            onSendClick = {
                coroutineScope.launch {
                    if (!ejecutandoHilo) {
                        ejecutandoHilo = true
                        val userMessage = message
                        message = ""
                        val data = ChatHelper().enviarMensaje(hiloid.toString(), userMessage)
                        ChatViewModel.procesarMensajes(data)
                        ejecutandoHilo = false
                    } else {
                        Toast.makeText(context, "Espere un momento", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            isSending = ejecutandoHilo
        )
    }
}

@Composable
fun ChatTopBar(onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6a815b))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("🦦", fontSize = 20.sp)
        }
        Text(
            text = "NutrIA",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun MessageBubble(messageText: String, isUserMessage: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isUserMessage) darkGreen else lightGreen,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = messageText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun ChatInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Escribe algo..." , color = DarkText) },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = lightGreen,
                unfocusedContainerColor = lightGreen,
                disabledContainerColor = lightGreen,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSendClick,
            enabled = !isSending && message.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(darkGreen)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar",
                tint = Color.White
            )
        }
    }
}

private suspend fun enviarMensajeIniciar(
    context: Context,
    viewModel: OffLineUserViewModel,
    hiloid: String
): String {
    val email = recuperarEMAIL(context)
    val user = viewModel.getUser(email.toString())
    val mensajeInicial = "Hola, soy ${user.name}, tengo ${user.edad} años, mido ${user.altura} cm y peso ${user.peso} kg" + "" +
            "Mi imc es ${user.imc}, mi tmb es ${user.tmb} y mi frecuencia de ejercicio es ${user.frecuencia}" + "mi condicion de salud es ${user.condicion}" +
            " soy ${if (user.sexo) "hombre" else "mujer"}"
    return ChatHelper().enviarMensaje(hiloid, mensajeInicial)
}

private fun recuperarEMAIL(context: Context): String? {
    val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
    return sharedPref.getString("email", "")
}
