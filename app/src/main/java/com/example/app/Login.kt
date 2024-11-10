package com.example.app

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.model.CondicionesMedicas
import com.example.app.model.User
import com.example.app.ui.theme.AppTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID

@Composable
fun Login(
    modifierExt: Modifier = Modifier,
    nexScreen: () -> Unit ={}
) {
    val context = LocalContext.current
    var switchEncendido by remember { mutableStateOf(value = true) }
    var correo by remember { mutableStateOf(value = "") }
    var nombreUsua by remember { mutableStateOf(value = "") }
    var contra by remember { mutableStateOf(value = "") }
    var confirmCotra by remember { mutableStateOf(value = "") }
    val titulo = stringResource(
        if(switchEncendido){
            R.string.iniciar_sesion
        }else{
            R.string.registrarse
        }
    )
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifierExt
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
        Text (
            text = titulo,
            fontSize = 35.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Switch(
            checked = switchEncendido,
            onCheckedChange = {
                switchEncendido = !switchEncendido
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        val imagen = painterResource(R.drawable.logo)
        Image(
            painter = imagen,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentDescription = null
        )
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
            },
            label = {
                Text(
                    stringResource(R.string.correo_electr)
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        if(!switchEncendido){
            OutlinedTextField(
                value = nombreUsua,
                onValueChange = {
                    nombreUsua = it
                },
                label = {
                    Text(
                        stringResource(R.string.nombre_usuario)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        OutlinedTextField(
            value = contra,
            onValueChange = {
                contra = it
            },
            label = {
                Text(
                    stringResource(R.string.password)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        if(!switchEncendido){
            OutlinedTextField(
                value = confirmCotra,
                onValueChange = {
                    confirmCotra = it
                },
                label = {
                    Text(
                        stringResource(R.string.confirmar_contra)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
        }
        OutlinedButton(
            onClick =  {
                if (switchEncendido){
                    // INICIAR SESION
                    leerUsuario(
                        context = context,
                        email = correo,
                        password = contra,
                        nextScreen = nexScreen
                    )
                }else{
                    // REGISTRARSE
                    val user = User(correo, nombreUsua, contra, CondicionesMedicas.saludable)
                    validarUsuario(context, user, nexScreen)
                }
        },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ){
            Text(text = "SIGUIENTE")
        }
    }
}

private fun registrarUsuario(usuario: User){
    val reference = Firebase.database.getReference("usuarios")

    var uniqueID = UUID.randomUUID().toString()
    val idReference = reference.child(uniqueID)
    idReference.setValue(usuario)
}

private fun leerUsuario(context: Context, email: String, password: String, nextScreen: () -> Unit ={}){
    val reference = Firebase.database.getReference("usuarios")

    reference.get().addOnSuccessListener {
        val filtered = it.children.filter {
            it.getValue(User::class.java)?.email.equals(
                email
                ,true
            )
        }

        if (filtered.isEmpty()){
            Toast.makeText(context, "Usuario no existe",Toast.LENGTH_SHORT).show()
        } else {
            val user = filtered.get(0).getValue(User::class.java)
            if (user?.password.equals(password,true)){
                nextScreen.invoke()
            } else {
                Toast.makeText(context, "Contraseña incorrecta",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun validarUsuario(context: Context, usuario: User, nextScreen: () -> Unit ={}){
    val reference = Firebase.database.getReference("usuarios")
    reference.get().addOnSuccessListener {
        val filtered = it.children.filter {
            it.getValue(User::class.java)?.email.equals(
                usuario.email
                ,true
            )
        }

        if (filtered.isNotEmpty()){
            Toast.makeText(context, "El correo ya existe",Toast.LENGTH_SHORT).show()
        } else {
            registrarUsuario(usuario)
            Toast.makeText(context, "Usuario creado",Toast.LENGTH_SHORT).show()
            nextScreen.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Login()
    }
}