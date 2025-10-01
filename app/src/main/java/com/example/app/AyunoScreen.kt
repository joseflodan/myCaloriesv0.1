package com.example.app
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import kotlinx.coroutines.delay

@Composable
fun AyunoScreen() {
    //estados para el temporizador
    var pickerValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }
    var displayTime by remember { mutableStateOf("00:00:00") }
    var isAyunoActive by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }

    //paleta de colores
    val secondarySage = Color(0xFF6a815b)
    val primarySage =   Color(0xFFF7FDF7)
    val background = Color(0xFFa9ba9d)
    val DarkText = Color(0xFF232F27)

    // Parámetros del arco
    val strokeWidth = 24.dp
    val screenWith = 300.dp
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
        pickerValue = FullHours(0, 0)
        displayTime = "00:00:00"
        isAyunoActive = false
        progress = 0f
    }

    LaunchedEffect(isAyunoActive) {
        if (isAyunoActive) {
            val duration = "${pickerValue.hours}:${pickerValue.minutes}"
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
            .background( color = background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Canvas para mostrar el arco de progreso
        Canvas(
            modifier = Modifier
                .size(screenWith)
                .padding(10.dp),
        ) {
            val canvasSize = screenWith.toPx()
            val strokeWidthPx = strokeWidth.toPx()

            // Dibuja el arco de fondo
            drawArc(
                color = secondarySage,
                startAngle = BackgroundStartAngle,
                sweepAngle = BackgroundSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidthPx + 10f, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(x = 0f, y = -canvasSize / 12f)
            )

            // Dibuja el arco de progreso
            drawArc(
                color = primarySage,
                startAngle = BackgroundStartAngle,
                sweepAngle = (progress * BackgroundSweepAngle),
                useCenter = false,
                style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(x = 0f, y = -canvasSize / 12f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = displayTime,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text("Duración (HH:MM)", color = DarkText, fontSize = 20.sp)

        HoursNumberPicker(
            dividersColor = Color(0xFFA2A29E),
            leadingZero = true,
            value = pickerValue,
            onValueChange = {
                pickerValue = it
            },
            hoursDivider = {
                Text(
                    modifier = Modifier.size(34.dp),
                    textAlign = TextAlign.Center,
                    text = ":",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        )


        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val duration = "${pickerValue.hours}:${pickerValue.minutes}"
                val durationInMillis = parseTimeToMillis(duration)

                if (durationInMillis > 0) {
                    isAyunoActive = true
                } else {
                    displayTime = "Tiempo inválido"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = secondarySage,
                contentColor = DarkText
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Text(
                text = "EMPEZAR AYUNO",
                color = DarkText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { resetAyuno() },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = secondarySage,
                contentColor = DarkText
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Text(
                text = "REINICIAR AYUNO",
                color = DarkText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewAyunoScreen() {
    AyunoScreen()
}
