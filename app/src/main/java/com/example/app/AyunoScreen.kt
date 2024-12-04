package com.example.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AyunoScreen(
    modifier: Modifier = Modifier,
    fillColor: Color = Color(color = 0xFFa07054),
    backgroundColor: Color = Color(color =0xFF6b4a38),
    strokeWidth: Dp = 8.dp,
    calorias: Float = 0f
) {
    val CALORIAS_PROVISIONAL = 2000F

    val BackgroundStartAngle = 140f
    val BackgroundSweepAngle = 260f
    val TotalSweepAngle = 260.0

    val percentage = calorias/CALORIAS_PROVISIONAL

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
        val screenHeight = configuration.screenHeightDp.dp
        val screenWith = configuration.screenWidthDp.dp - 30.dp

        Canvas(
            modifier = Modifier
                .size(150.dp)
                .padding(10.dp),
        ) {
            drawArc(
                color = backgroundColor,
                startAngle = BackgroundStartAngle,
                sweepAngle = BackgroundSweepAngle,
                useCenter = false,
                style = Stroke((strokeWidth+10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 2f)
            )

            drawArc(
                color = fillColor,
                startAngle = BackgroundStartAngle,
                sweepAngle = (percentage * TotalSweepAngle).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 2f)
            )
        }
        Text(
            text = "YA ESTAS AYUNANDO!!",
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(
                    x = screenWith / 3f - screenWith / 7,
                    y = -screenWith / 0.9f
                )
        )

        Text(
            text = "TIEMPO RESTANTE",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .offset(
                    x = screenWith / 2 - screenWith / 7,
                    y = -screenWith / 1.9f
                )
        )

        Text(
            text = "HH:MM:SS",
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(
                    x = screenWith / 2f - screenWith / 7,
                    y = -screenWith / 2f
                )
        )
        OutlinedButton(
            onClick =  { },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xFFa07054))
        ){
            Text(
                text = "DEJAR DE AYUNAR",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "INICIO"
        )
        TextField(value = "", onValueChange = {})

    }
}

@Preview(showBackground = true)
@Composable
fun AyunoPreview() {
    AyunoScreen(

    )
}
