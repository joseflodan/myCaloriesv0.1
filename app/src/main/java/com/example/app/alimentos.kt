package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun alimentos() {
    var foodInput by remember { mutableStateOf("") }
    val foodList = remember { mutableStateListOf<String>() }

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
            onValueChange = { foodInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF8C6E60), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar alimento
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF8C6E60), RoundedCornerShape(8.dp))
                .clickable {
                    if (foodInput.isNotEmpty()) {
                        foodList.add(foodInput)
                        foodInput = ""
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "+", style = TextStyle(color = Color.White, fontSize = 24.sp))
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

@Preview(showBackground = true)
@Composable
fun alimentosPreview() {
    alimentos()
}