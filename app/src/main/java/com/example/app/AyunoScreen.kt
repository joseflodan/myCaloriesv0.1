package com.example.app
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AyunoScreen() {
    var duration by remember { mutableStateOf("00:00") }
    var displayTime by remember { mutableStateOf("00:00:00") }
    var isAyunoActive by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    val backgroundColor = Color(0xFF6b4a38)
    val fillColor = Color(0xFFa07054)
    val strokeWidth = 24.dp  // Aumentamos el tamaño del arco
    val screenWith = 300.dp  // Aumentamos aún más el tamaño del arco
    val BackgroundStartAngle = 140f
    val BackgroundSweepAngle = 260f
    val TotalSweepAngle = 360f

    fun parseTimeToMillis(time: String): Long {
        val parts = time.split(":")
        return if (parts.size == 2) {
            val hours = parts[0].toIntOrNull() ?: 0
            val minutes = parts[1].toIntOrNull() ?: 0
            (hours * 3600 + minutes * 60) * 1000L
        } else {
            0L
        }
    }

    fun resetAyuno() {
        duration = "00:00"
        displayTime = "00:00:00"
        isAyunoActive = false
        progress = 0f
    }

    LaunchedEffect(isAyunoActive) {
        if (isAyunoActive) {
            val durationInMillis = parseTimeToMillis(duration)
            var timeRemaining = durationInMillis

            if (timeRemaining <= 0) {
                isAyunoActive = false
                displayTime = "00:00:00"
                return@LaunchedEffect
            }

            while (timeRemaining > 0 && isAyunoActive) {
                delay(1000L)
                timeRemaining -= 1000L

                val hours = (timeRemaining / 3600000).toInt()
                val minutes = ((timeRemaining % 3600000) / 60000).toInt()
                val seconds = ((timeRemaining % 60000) / 1000).toInt()
                displayTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                // Calculamos el progreso del arco como porcentaje
                progress = 1f - (timeRemaining.toFloat() / durationInMillis.toFloat())
            }

            if (timeRemaining <= 0) {
                isAyunoActive = false
                displayTime = "00:00:00"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFd5bdaf), Color(0xFFedede9))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Canvas para mostrar el arco de progreso (ahora más grande)
        Canvas(
            modifier = Modifier
                .size(screenWith)
                .padding(10.dp),
        ) {
            val canvasSize = screenWith.toPx()
            val strokeWidthPx = strokeWidth.toPx()

            // Dibuja el arco de fondo (gray)
            drawArc(
                color = backgroundColor,
                startAngle = BackgroundStartAngle,
                sweepAngle = BackgroundSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidthPx + 10f, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(x = 0f, y = -canvasSize / 12f)
            )

            // Dibuja el arco de progreso (verde) con límite de 260 grados
            drawArc(
                color = fillColor,
                startAngle = BackgroundStartAngle,
                sweepAngle = (progress * BackgroundSweepAngle), // Limita el arco a 260 grados
                useCenter = false,
                style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(x = 0f, y = -canvasSize / 12f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar el temporizador
        Text(
            text = displayTime,
            fontSize = 30.sp,  // Hacemos el temporizador más grande
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input para la duración del ayuno
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = duration,
                onValueChange = {
                    if (it.length <= 5) {
                        duration = it
                    }
                },
                label = { Text("Duración (HH:MM)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        // Botón para iniciar el ayuno
        Button(
            onClick = {
                if (duration.isNotEmpty()) {
                    val durationInMillis = parseTimeToMillis(duration)

                    if (durationInMillis > 0) {
                        isAyunoActive = true
                    } else {
                        displayTime = "Tiempo inválido"
                    }
                } else {
                    displayTime = "Rellena el campo"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFa07054)) // Color del botón
        ) {
            Text("Empezar Ayuno")
        }

        Spacer(modifier = Modifier.height(16.dp))

// Botón para reiniciar el ayuno
        Button(
            onClick = { resetAyuno() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFa07054)) // Color del botón
        ) {
            Text("Reiniciar Ayuno")
        }

    }
}


@Composable
@Preview(showBackground = true)
fun PreviewAyunoScreen() {
    AyunoScreen()
}
