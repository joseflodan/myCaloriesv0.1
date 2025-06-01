package com.example.app

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.data.alimentos.AlimentAI
import com.example.app.ia.ChatHelper
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.OffLineAlimentViewModel
import com.example.app.viewmodel.OffLineCalenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun alimentos(
    alimentViewModel: OffLineAlimentViewModel = viewModel(factory = AppViewModelProvider.Factory),
    calendarViewModel: OffLineCalenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {
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
            capturedImageUri = uri
            coroutineScope.launch {
                val data = ChatHelper().cargarImagen(context, capturedImageUri)
                alimentViewModel.getProcesOpenIaResponse(context ,data)
            }
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
    val openAlertDialog = remember { mutableStateOf(false) }

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

        if (alimentViewModel.listadeAlimentos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFF8C6E60), RoundedCornerShape(8.dp))
                    .clickable {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            )
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
        }

        alimentViewModel.listadeAlimentos.forEach { alimento ->
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable{
                    alimentViewModel.updateSelectedAliments(alimento)
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(alimento.nombre.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.weight(weight = 1f)
                    )

                val validacion = stringResource(R.string.no_data_alimentos)
                if(!alimento.nombre.equals(validacion, ignoreCase = true)){
                    Checkbox(alimento.seleccionado,
                        onCheckedChange = {
                            alimentViewModel.updateSelectedAliments(alimento)
                        },
                        modifier = Modifier.weight(weight = 1f)
                    )
                }
            }

            /*       if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
            )
        } */
        }
        alimentViewModel.listadeAlimentos.forEach{}

        if (alimentViewModel.listadeAlimentos.isNotEmpty()){
            val alimentosSeleccionados = alimentViewModel.listadeAlimentos.filter { it.seleccionado }
            Button(
                onClick = {
                    if (alimentosSeleccionados.isEmpty()){
                        Toast.makeText(context, "No hay alimentos seleccionados", Toast.LENGTH_SHORT).show()
                    }else{
                        openAlertDialog.value = true}
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFa07054)) // Color del botón
            ) {
                Text("GUARDAR")
            }
        }
        when{
            openAlertDialog.value ->{
                val alimentosSeleccionados = alimentViewModel.listadeAlimentos.filter { it.seleccionado }
                confirmationDialoge(
                    listadeAlimentos = alimentosSeleccionados,
                    guardar = {
                        Toast.makeText(context, "Alimentos guardados", Toast.LENGTH_SHORT).show()
                        guardarAlimentos(context,alimentosSeleccionados, calendarViewModel,coroutineScope)
                        openAlertDialog.value = false
                    },
                    onDismissRequest ={
                        openAlertDialog.value = false
                    }
                )
            }
        }
    }
}

fun guardarAlimentos(
    context: Context,
    listadeAlimentos: List<AlimentAI>,
    calendarViewModel: OffLineCalenViewModel,
    coroutineScope: CoroutineScope
){
    val email = recuperarEMAIL(context).toString()
    listadeAlimentos.forEach{alimentAI ->
        coroutineScope.launch {
            calendarViewModel.updateCalories(
                email = email,
                calorias = alimentAI.Kcal,
                producto = alimentAI.nombre
            )
        }
    }
}

@Composable
fun confirmationDialoge(
    listadeAlimentos: List<AlimentAI>,
    guardar: () -> Unit,
    onDismissRequest: () -> Unit,
) {

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "ESTOS SON LOS ALIMENTOS QUE SELECCIONASTE:" ,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
            LazyColumn(Modifier.height(150.dp)) {
                items(listadeAlimentos){alimento ->
                    Text(
                        alimento.nombre.replaceFirstChar { it.uppercase() },
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Text(
                text = "SON CORRECTOS?" ,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        guardar()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFa07054)) // Color del botón
                ) {
                    Text("SI")
                }

                Button(
                    onClick = {onDismissRequest()},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFa07054)) // Color del botón
                ) {
                    Text("NO")
                }
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

private fun recuperarEMAIL (context: Context): String?{
    val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
    return sharedPref.getString("email","")
}