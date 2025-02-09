package com.example.app


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.MyApp.Companion.EJERCICIO
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.OffLineUserViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCscreen(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    fillColor: Color = Color(color = 0xFFa07054),
    backgroundColor: Color = Color(color = 0xFF6b4a38),
    viewModel: OffLineUserViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    var sexo by remember { mutableStateOf(value = true) } // true -> masculino
    var altura by remember { mutableStateOf(value = "") }
    var peso by remember { mutableStateOf(value = "") }
    var edad by remember { mutableStateOf(value = "") }
    var ejercicioSeleccionado by remember { mutableStateOf(EJERCICIO[0]) }
    var expanded by remember { mutableStateOf(false) }

    val pattern = remember { Regex("^\\d+\$") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(color = 0xFFd5bdaf),
                        Color(color = 0xFFedede9)
                    )
                )
            )
            .fillMaxSize()
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp - 20.dp
        val screenWith = configuration.screenWidthDp.dp - 30.dp
        val interactionSource = remember { MutableInteractionSource() }

        // Imagenes y botones para sexo
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)) {
            val imagenHombre = if (sexo) R.drawable.man_filled else R.drawable.man_unfilled
            val imagenMujer = if (sexo) R.drawable.woman_unfilled else R.drawable.woman_filled

            Image(
                painter = painterResource(imagenHombre),
                contentDescription = "",
                modifier = Modifier.height(75.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { sexo = true },
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(fillColor)
            )

            VerticalDivider(
                color = fillColor,
                thickness = 5.dp,
                modifier = Modifier.height(75.dp)
            )

            Image(
                painter = painterResource(imagenMujer),
                contentDescription = "",
                modifier = Modifier.height(75.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { sexo = false }
                    .background(Color.Transparent),
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(fillColor)
            )
        }

        // Campos de entrada para altura y peso
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Altura: ", fontSize = 20.sp)
            TextField(
                value = altura,
                onValueChange = {
                    if (it.isEmpty() || it.matches(pattern)) {
                        altura = it
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text(text = "Cm", fontSize = 20.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Peso: ", fontSize = 20.sp)
            TextField(
                value = peso,
                onValueChange = {
                    if (it.isEmpty() || it.matches(pattern)) {
                        peso = it
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text(text = "Kg", fontSize = 20.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Edad: ", fontSize = 20.sp)
            TextField(
                value = edad,
                onValueChange = {
                    if (it.isEmpty() || it.matches(pattern)) {
                        edad = it
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text(text = "Años", fontSize = 20.sp)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp),) {
            Text(text = "Frecuencia de Ejercicio: ", fontSize = 20.sp)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = ejercicioSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 10.dp)
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    EJERCICIO.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                ejercicioSeleccionado = item
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        // Canvas con los arcos
/*
        Canvas(
            modifier = Modifier
                .size(130.dp)
                .padding(10.dp)
        ) {
            drawArc(
                color = backgroundColor,
                startAngle = 140f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth + 10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),

            )

            // Calcular el IMC
            val imc = if (altura.isNotEmpty() && peso.isNotEmpty()) {
                val alturaAlCuadrado = (altura.toFloat() / 100) * (altura.toFloat() / 100)
                peso.toFloat() / alturaAlCuadrado
            } else {
                0f
            }

            // Primer arco (15 a 17)
            val firstArcPercentage = when {
                imc >= 15 && imc <= 17 -> (imc - 15) / 2f
                imc > 17 -> 1f
                else -> 0f
            }
            drawArc(
                color = fillColor,
                startAngle = 140f,
                sweepAngle = 84f * firstArcPercentage,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
            )

            drawArc(
                color = backgroundColor,
                startAngle = 230f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth + 10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
            )

            // Segundo arco (17 a 23)
            val secondArcPercentage = when {
                imc > 17 && imc <= 23 -> (imc - 17) / 6f
                imc > 23 -> 1f
                else -> 0f
            }
            drawArc(
                color = fillColor,
                startAngle = 230f,
                sweepAngle = 84f * secondArcPercentage,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
            )

            drawArc(
                color = backgroundColor,
                startAngle = 320f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth + 10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
            )

            // Tercer arco (23 a 38)
            val thirdArcPercentage = when {
                imc > 23 -> (imc - 23) / 15f
                else -> 0f
            }
            drawArc(
                color = fillColor,
                startAngle = 320f,
                sweepAngle = 84f * thirdArcPercentage,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
            )
        }

        // Texto para IMC
        Text(
            text = "IMC: ",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

 */
        var imc = 0.0f
        // Mostrar IMC calculado
        if (altura.isNotEmpty() && peso.isNotEmpty()) {
            val alturaAlCuadrado = (altura.toFloat() / 100) * (altura.toFloat() / 100)
            imc = peso.toFloat() / alturaAlCuadrado
        }
/*            Text(
                text = "%.2f".format(imc),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                .align(Alignment.CenterHorizontally)
            )

            // Mostrar el estado del IMC centrado
            val imcStatus = when {
                imc < 17 -> "Desnutrición"
                imc in 17.0..23.0 -> "Normal"
                imc > 23 -> "Sobrepeso"
                else -> "No Disponible"
            }

            Text(
                text = imcStatus,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // Centrado horizontal
            )

*/
        OutlinedButton(
            onClick =  {
                coroutineScope.launch {
                        val email = recuperarEMAIL(context).toString()
                        viewModel.guardarDatos(email,sexo,altura,peso,edad,imc,ejercicioSeleccionado)
                    }
                Toast.makeText(context, "GUARDADO", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ){
            Text(text = "GUARDAR")
        }
    }
}


private fun recuperarEMAIL (context: Context): String?{
    val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
    return sharedPref.getString("email","")
}


@Preview(showBackground = true)
@Composable
fun IMCPreview() {
    IMCscreen(

    )
}
