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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.viewmodel.AppViewModelProvider
import com.example.app.viewmodel.OffLineUserViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ContadorScreen(
    modifier: Modifier = Modifier,
    fillColor: Color = Color(color = 0xFFa07054),
    backgroundColor: Color = Color(color =0xFF6b4a38),
    strokeWidth: Dp = 8.dp,
    viewModel: OffLineUserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val BackgroundStartAngle = 140f
    val BackgroundSweepAngle = 260f
    val TotalSweepAngle = 260.0
    val context = LocalContext.current
    val email = recuperarEMAIL(context).toString()

    val tmb = viewModel.getTMB(email)

    val calorias = viewModel.getCalorias(email)


    val percentage = calorias/tmb

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
                topLeft = Offset(x = 0f, y = - screenHeight.value / 3f)
            )

            drawArc(
                color = fillColor,
                startAngle = BackgroundStartAngle,
                sweepAngle = (percentage * TotalSweepAngle).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(screenWith.toPx(), screenWith.toPx()),
                topLeft = Offset(x = 0f, y = - screenHeight.value / 3f)
            )
        }

        Text(
            text = "CALORIAS\nCONSUMIDAS",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .offset(
                    x = screenWith / 2 - screenWith / 7,
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
