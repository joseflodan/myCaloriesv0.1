package com.example.app

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.app.ia.ChatHelper
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.OffLineAlimentViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun alimentos(
    alimentViewModel: OffLineAlimentViewModel = viewModel(factory = AppViewModelProvider.Factory),
    ) {
    var foodInput by remember { mutableStateOf(TextFieldValue("")) }
    val foodList = remember { mutableStateListOf<String>() }
    val suggestions = alimentViewModel.getAliments()

    var filteredSuggestions by remember { mutableStateOf(suggestions) }
    var showSuggestions by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val coroutineScope = rememberCoroutineScope()

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            coroutineScope.launch{
                val data = ChatHelper().consultarAlimentosEnFoto(context, capturedImageUri)
                Log.d("Prueba","Respuesta: $data")
            }
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDEC3B5)) // Fondo similar al de la imagen
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ALIMENTOS NO PROCESADOS",
            style = TextStyle(fontSize = 20.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada de texto
        BasicTextField(
            value = foodInput,
            onValueChange = { newValue ->
                if (newValue.text != foodInput.text) {
                    foodInput = newValue
                    // Show suggestions only when typing and input is not empty
                    showSuggestions = newValue.text.isNotEmpty()
                    // Update filtered suggestions based on current input
                    filteredSuggestions = if (newValue.text.isEmpty()) {
                        suggestions
                    } else {
                        suggestions.filter { it.nombre.contains(newValue.text, ignoreCase = true) }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF8C6E60), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .focusRequester(focusRequester),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp)
        )

        // Automatically request focus when the UI is composed
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(8.dp))
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .animateContentSize()
        ) {
            if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(
                        items = filteredSuggestions,
                        key = { suggestion -> suggestion.nombre } // Using the suggestion string as the key
                    ) { suggestion ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Update text with selected suggestion and move cursor to the end
                                    foodInput = TextFieldValue(
                                        text = suggestion.nombre,
                                        selection = TextRange(suggestion.nombre.length)
                                    )
                                    // Hide suggestions after selection
                                    showSuggestions = false
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = suggestion.nombre,
                                style = TextStyle(fontSize = 18.sp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            } else if (showSuggestions && filteredSuggestions.isEmpty()) {
                Text(
                    text = "No suggestions available",
                    style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar alimento
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF8C6E60), RoundedCornerShape(8.dp))
                .clickable {
/*                    if (foodInput.text.isNotEmpty()) {
                        foodList.add(foodInput.text)
                        foodInput = TextFieldValue("")
                    }*/
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "+", style = TextStyle(color = Color.White, fontSize = 24.sp))
        }

        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de alimentos
        Column(modifier = Modifier.fillMaxWidth()) {
            foodList.forEach { food ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFF8C6E60), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = food, style = TextStyle(color = Color.White, fontSize = 18.sp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}