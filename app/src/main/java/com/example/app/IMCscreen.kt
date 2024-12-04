package com.example.app


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun IMCscreen(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    fillColor: Color = Color(color = 0xFFa07054),
    backgroundColor: Color = Color(color = 0xFF6b4a38),
) {
    val BackgroundStartAngle = 140f
    val TotalSweepAngle = 260.0
    var sexo by remember { mutableStateOf(value = true) } //true -> masculino
    var altura by remember { mutableStateOf(value = "") }
    var peso by remember { mutableStateOf(value = "") }
    val pattern = remember { Regex("^\\d+\$") }

    Column(
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
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp - 20.dp
        val screenWith = configuration.screenWidthDp.dp - 30.dp
        val interactionSource = remember { MutableInteractionSource() }

        // Imagenes y botones para sexo
        Row(modifier = Modifier.offset(x = 0.dp, y = -150.dp)) {
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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.offset(x = 0.dp, y = -100.dp)) {
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

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.offset(x = 0.dp, y = -100.dp)) {
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

        // Canvas con los arcos
        Canvas(
            modifier = Modifier
                .size(150.dp)
                .padding(10.dp)
        ) {
            drawArc(
                color = backgroundColor,
                startAngle = 140f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth + 10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = -screenHeight.value / 8f)
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
                topLeft = Offset(x = 0f, y = -screenHeight.value / 8f)
            )

            drawArc(
                color = backgroundColor,
                startAngle = 230f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth + 10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = -screenHeight.value / 8f)
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
                topLeft = Offset(x = 0f, y = -screenHeight.value / 8f)
            )

            drawArc(
                color = backgroundColor,
                startAngle = 320f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth + 10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = -screenHeight.value / 8f)
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
                topLeft = Offset(x = 0f, y = -screenHeight.value / 8f)
            )
        }

        // Texto para IMC
        Text(
            text = "IMC: ",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .offset(x = screenWith / 2 - screenWith / 20, y = -screenWith / 4f)
        )

        // Mostrar IMC calculado
        if (altura.isNotEmpty() && peso.isNotEmpty()) {
            val alturaAlCuadrado = (altura.toFloat() / 100) * (altura.toFloat() / 100)
            val imc = peso.toFloat() / alturaAlCuadrado

            Text(
                text = "%.2f".format(imc),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .offset(x = screenWith / 2 - screenWith / 7, y = -screenWith / 4f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IMCPreview() {
    IMCscreen(

    )
}
