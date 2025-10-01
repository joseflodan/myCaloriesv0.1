package com.example.app

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.data.user.User
import com.example.app.ui.theme.AppTheme
import com.example.app.utils.PrefsHelper
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.OffLineUserViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifierExt: Modifier = Modifier,
    nextScreen: () -> Unit = {},
    primerInicioSesion: () -> Unit = {},
    viewModel: OffLineUserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val headerColor = Color(0xFFA7B099)
    val context = LocalContext.current
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val conditions = listOf("Saludable", "Diabetes", "Sobrepeso")
    var expanded by remember { mutableStateOf(false) }
    var selectedCondition by remember { mutableStateOf(conditions.first()) }
    val title = if (isLoginMode) stringResource(R.string.iniciar_sesion) else stringResource(R.string.registrarse)
    val buttonText = if (isLoginMode) stringResource(R.string.iniciar) else stringResource(R.string.siguiente)
    val toggleText = if (isLoginMode) stringResource(R.string.crear_cuenta) else stringResource(R.string.tener_cuenta)
    val coroutineScope = rememberCoroutineScope()
    val iconColor = Color(color = 0xFF000000)

    val savedEmail = PrefsHelper.getEmail(context)
    LaunchedEffect(savedEmail) {
        if (!savedEmail.isNullOrEmpty()) nextScreen()
    }

    Column(
        modifier = modifierExt
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(headerColor, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "My Calories", fontSize = 45.sp,color = Color.Black)
                Text(text = title, fontSize = 24.sp, color = Color.White)
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = !isLoginMode,
                        onCheckedChange = { isLoginMode = !it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.correo_electr)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = iconColor
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
            )
            if (!isLoginMode) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(stringResource(R.string.nombre_usuario)) },
                    leadingIcon ={
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = iconColor
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)

                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_passw),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = iconColor
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
            )
            if (!isLoginMode) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.confirmar_contra)) },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_passw),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = iconColor
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
                )
                Spacer(Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCondition,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.enfermedad)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_health),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = iconColor
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                TrailingIcon(expanded = expanded)
                            }
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        conditions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    selectedCondition = opt
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    if (isLoginMode) {
                        coroutineScope.launch {
                            val session = viewModel.leerUsuario(email, password)
                            if (session.pasar) {
                                PrefsHelper.saveEmail(context, email)
                                nextScreen()
                            } else {
                                Toast.makeText(context, session.mensaje, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        when {
                            email.isBlank() -> Toast.makeText(context, "Correo vacío", Toast.LENGTH_SHORT).show()
                            username.isBlank() -> Toast.makeText(context, "Nombre vacío", Toast.LENGTH_SHORT).show()
                            password.isBlank() -> Toast.makeText(context, "Contraseña vacía", Toast.LENGTH_SHORT).show()
                            confirmPassword.isBlank() -> Toast.makeText(context, "Confirmar contraseña vacía", Toast.LENGTH_SHORT).show()
                            else -> {
                                val user = User(email = email, name = username, password = password, condicion = selectedCondition)
                                coroutineScope.launch {
                                    if (viewModel.validarUsuario(user)) {
                                        Toast.makeText(context, "Usuario creado", Toast.LENGTH_SHORT).show()
                                        PrefsHelper.saveEmail(context, email)
                                        primerInicioSesion()
                                    } else {
                                        Toast.makeText(context, "El correo ya existe", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = headerColor)
            ) {
                Text(text = buttonText, color = Color.White)
            }
        }
    }
}

private fun registrarUsuario(usuario: User) {
    val ref = Firebase.database.getReference("usuarios").child(UUID.randomUUID().toString())
    ref.setValue(usuario)
}

private fun validarUsuario(context: Context, usuario: User, next: () -> Unit = {}) {
    val ref = Firebase.database.getReference("usuarios")
    ref.get().addOnSuccessListener {
        val exists = it.children.any { snap ->
            snap.getValue(User::class.java)?.email.equals(usuario.email, true)
        }
        if (exists) Toast.makeText(context, "El correo ya existe", Toast.LENGTH_SHORT).show()
        else {
            registrarUsuario(usuario)
            Toast.makeText(context, "Usuario creado", Toast.LENGTH_SHORT).show()
            next()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AppTheme { LoginScreen() }
}
