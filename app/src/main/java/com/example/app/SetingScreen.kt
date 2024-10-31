package com.example.app

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetingScreen(
    modifier: Modifier = Modifier,
    nexScreen: () -> Unit = {}

){
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
             onClick = {},
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
            onClick = {},
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
@Preview (showBackground = true)
@Composable
fun SetingPreview (){
    SetingScreen()

}
