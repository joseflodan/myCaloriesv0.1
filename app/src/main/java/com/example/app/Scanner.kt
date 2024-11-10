package com.example.app

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.model.FoodResult
import com.example.app.viewmodel.FoodApiViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Scanner(
    modifier: Modifier = Modifier
){
    var codigoCapturdo by remember { mutableStateOf(value = "") }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

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
fun ResultScreen(respuesta: FoodResult) {
    Column {
        Text(respuesta.product?.brands.toString())
        Text(respuesta.product?.productName.toString())
        Text(respuesta.product?.imageUrl.toString())
        Text(respuesta.product?.nutriments?.energyKcal.toString())
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
