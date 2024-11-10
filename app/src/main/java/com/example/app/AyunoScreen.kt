package com.example.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AyunoScreen(
    modifier: Modifier = Modifier,
    percentage: Float = 0f,
    fillColor: Color = Color.Green,
    backgroundColor: Color = Color.Gray,
    strokeWidth: Dp = 8.dp
) {
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


        val BackgroundStartAngle = 140f
        val BackgroundSweepAngle = 260f
        val CircleRadius = 10f
        val AngleOffset = 50.0
        val TotalSweepAngle = 260.0

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
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
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

            val offsetX = screenHeight.value / 50f
            val offsetY = -screenHeight.value / 7.5f
            val angleInDegrees = (percentage * TotalSweepAngle) + AngleOffset
            val radius = (size.height / 2)
            val x = -(radius * sin(Math.toRadians(angleInDegrees))).toFloat() + (size.width / 2)
            val y = (radius * cos(Math.toRadians(angleInDegrees))).toFloat() + (size.height / 2)

            drawCircle(
                color = Color.White,
                radius = CircleRadius,
                center = Offset(offsetX,offsetY)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AyunoPreview() {
    AyunoScreen(
        percentage = 0.70f,
        fillColor = Color(android.graphics.Color.parseColor("#4DB6AC")),
        backgroundColor = Color(android.graphics.Color.parseColor("#90A4AE")),
        strokeWidth = 10.dp
    )
}
