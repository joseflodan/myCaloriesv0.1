package com.example.app

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.OffLineCalenViewModel
import com.example.app.viewmodel.OffLineUserViewModel

@Composable
fun ContadorScreen(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    userviewModel: OffLineUserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    calenviewModel: OffLineCalenViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val strokeWidth = 24.dp
    val BackgroundStartAngle = 140f
    val BackgroundSweepAngle = 260f
    val TotalSweepAngle = 260.0
    val context = LocalContext.current

    val secondarySage = Color(0xFF6a815b)
    val primarySage =   Color(0xFFF7FDF7)
    val background = Color(0xFFa9ba9d)
    val DarkText = Color(0xFF232F27)

    val email = recuperarEMAIL(context).toString()
    val tmb = userviewModel.getTMB(email)
    val calorias = calenviewModel.getTodayCalories(email)
    val percentage = calorias/tmb

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(background)
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

            val canvasSize = screenWith.toPx()
            val strokeWidthPx = strokeWidth.toPx()

            drawArc(
                color = secondarySage,
                startAngle = BackgroundStartAngle,
                sweepAngle = BackgroundSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidthPx + 10f, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 3f)
            )
            drawArc(
                color = primarySage,
                startAngle = BackgroundStartAngle,
                sweepAngle = (percentage * TotalSweepAngle).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 3f)
            )
        }

        Text(
            text = "CALORIAS\nCONSUMIDAS",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = DarkText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(
                    x = screenWith / 2.25f - screenWith / 7,
                    y = -screenWith / 3f
                )
        )

        Text(
            text = "%.2f Kcal".format(calorias),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .offset(
                    x = screenWith / 2 - screenWith / 7f,
                    y = -screenWith / 3f
                )
        )
    }
}

private fun recuperarEMAIL (context: Context): String?{
    val sharedPref = context.getSharedPreferences(MyApp.PREFERENCIAS, Context.MODE_PRIVATE)
    return sharedPref.getString("email","")
}

@Preview(showBackground = true)
@Composable
fun ContadorPreview() {
    ContadorScreen(
        strokeWidth = 10.dp
    )
}
