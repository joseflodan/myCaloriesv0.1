package com.example.app

import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app.model.FoodResult
import com.example.app.model.User
import com.example.app.viewmodel.FoodApiViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Scanner(
    modifier: Modifier = Modifier
){
    var codigoCapturdo by remember { mutableStateOf(value = "") }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val marsViewModel: FoodApiViewModel = viewModel()

    if (codigoCapturdo.isEmpty()){
        if (cameraPermissionState.status.isGranted) {
            CameraScreen(
                onChangeCode = {codigo ->
                    codigoCapturdo = codigo
                }
            )
        } else if (cameraPermissionState.status.shouldShowRationale) {
            Text(text = "no estan habilitados los permisos de la camara")
        } else {
            SideEffect {
                cameraPermissionState.run { launchPermissionRequest() }
            }
            Text("No Camera Permission")
        }
    }else{
        val marsViewModel: FoodApiViewModel = viewModel()
        marsViewModel.getFoodProduct(codigoCapturdo)
        ResultScreen(marsViewModel.respuesta)
    }
}

@Composable
fun CameraScreen(
    onChangeCode: (String) -> Unit ={}
) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder().build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                 BarcodeAnalyzer(context,
                     codigoLeido = {codigo ->
                         onChangeCode.invoke(codigo)
                     })
            )

            runCatching {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            }.onFailure {
                Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
            }
            previewView
        }
    )
}

@Composable
fun ResultScreen(respuesta: FoodResult, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier
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
    ){

        Text(
            text = "RESULTADO",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


        if(respuesta.product?.imageUrl?.isNotEmpty() == true) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(respuesta.product?.imageUrl).crossfade(true).build(),
                placeholder = painterResource(R.drawable.logo),
                contentDescription = "Descripcion de Imagen",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        Text(
            text = respuesta.product?.productName ?: "Nombre no disponible",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = respuesta.product?.brands ?: "Marca no disponible",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Calorías: ${respuesta.product?.nutriments?.energyKcal ?: "No disponible"}",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedButton(
            onClick =  {
                try{
                   val calorias = respuesta.product?.nutriments?.energyKcal?.toFloat()
                    calorias?.let { registrarCalorias(it) }
                } catch (_: Exception){}
                Toast.makeText(context, "GUARDADO", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ){
            Text(text = "GUARDAR")
        }
    }
}

fun registrarCalorias(calorias : Float){
    val reference = Firebase.database.getReference("usuarios")
    val idReference = reference.child(MyApp.USER_ID).child("calorias")

    //OPTENER CALORIAS ANTERIORES
    var caloriasTotales = 0F
    idReference.get().addOnSuccessListener {valorObtenido ->
       caloriasTotales =  valorObtenido.getValue(Float::class.java)!!
        caloriasTotales += calorias
        //REGISTRAR CALORIAS FINALES
        idReference.setValue(caloriasTotales)
    }
}
