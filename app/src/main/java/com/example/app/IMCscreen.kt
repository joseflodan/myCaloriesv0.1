package com.example.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun IMCscreen(
    modifier: Modifier = Modifier,
    percentage: Float = 0f,
    strokeWidth: Dp = 8.dp,
    fillColor: Color = Color(color = 0xFFa07054),
    backgroundColor: Color = Color(color =0xFF6b4a38),
) {
    val BackgroundStartAngle = 140f
    val TotalSweepAngle = 260.0

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
        val screenHeight = configuration.screenHeightDp.dp -20.dp
        val screenWith = configuration.screenWidthDp.dp - 30.dp

        Canvas(
            modifier = Modifier
                .size(150.dp)
                .padding(10.dp),
        ) {
            drawArc(
                color = backgroundColor,
                startAngle = 140f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth+10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value /8f)
            )

            drawArc(
                color = fillColor,
                startAngle = 140f,
                sweepAngle = 84f, // ESTE SERIA EL VALOR CON EL QUE JUGAR
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 8f)
            )

            drawArc(
                color = backgroundColor,
                startAngle = 230f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth+10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 8f)
            )

            drawArc(
                color = fillColor,
                startAngle = 230F,
                sweepAngle = 62f, // ESTE SERIA EL VALOR CON EL QUE JUGAR
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 8f)
            )

            drawArc(
                color = backgroundColor,
                startAngle = 320f,
                sweepAngle = 84f,
                useCenter = false,
                style = Stroke((strokeWidth+10.dp).toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 8f)
            )

            drawArc(
                color = fillColor,
                startAngle = BackgroundStartAngle,
                sweepAngle = (percentage * TotalSweepAngle).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 8f)
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
