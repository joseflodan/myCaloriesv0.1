package com.example.app

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    scanner: () -> Unit = {},
    ayunoInter: () -> Unit = {},
    contador: () -> Unit = {}
) {
    val context = LocalContext.current;
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
        val ArcColor = Color(0xFF60483a)

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWith = configuration.screenWidthDp.dp

        val imagen = ImageBitmap.imageResource(R.drawable.logo)

        Canvas(
            modifier = Modifier,
            onDraw = {
                drawImage(
                    image = imagen,
                    topLeft = Offset(x = -95f, y = -screenHeight.value / 30f)
                )
                drawArc(
                    topLeft = Offset(x = -screenHeight.value / 1.75f, -screenHeight.value / 4f),
                    color = ArcColor,
                    startAngle = 270f,
                    sweepAngle = 180f,
                    useCenter = false,
                    size = Size(screenHeight.value, screenHeight.value),
                    style = Stroke(width = 10f)
                )
            }
        )

        TextButton(
            onClick = {
                ayunoInter.invoke()
            },
            modifier = Modifier.offset(x = screenWith / 2 - screenWith / 6, y = -screenWith / 5.5f)
        ) {
            Icon(
                painter = painterResource(R.drawable.button1),
                contentDescription = null
            )
            Text(text = "AYUNO INTERMITENTE", color = Color.Black)
        }

        TextButton(
            onClick = {
                contador.invoke()
            },
            modifier = Modifier.offset(x = screenWith / 2.25f, y = -screenWith / 6)
        ) {
            Icon(
                painter = painterResource(R.drawable.button1),
                contentDescription = null
            )
            Text(text = " CONTADOR", color = Color.Black)
        }

//este es un comentario de prueva

        TextButton(
            onClick = {
                scanner.invoke()
                Toast.makeText(context, "SCANER presionado", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.offset(x = screenWith / 2, y = -screenWith / 7.5f)
        ) {
            Icon(
                painter = painterResource(R.drawable.button1),
                contentDescription = null
            )
            Text(text = " SCANER", color = Color.Black)
        }

        TextButton(
            onClick = {
                Toast.makeText(context, "IMC precionado", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.offset(x = screenWith / 2, y = -screenWith / 11.5f)
        ) {
            Icon(
                painter = painterResource(R.drawable.button1),
                contentDescription = null
            )
            Text(text = " IMC", color = Color.Black)
        }

        TextButton(
            onClick = {
                Toast.makeText(context, "CALENDARIO presionado", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.offset(x = screenWith / 2.25f, y = -screenWith / 18)
        ) {
            Icon(
                painter = painterResource(R.drawable.button1),
                contentDescription = null
            )
            Text(text = " CALENDARIO", color = Color.Black)
        }
        TextButton(
            onClick = {
                Toast.makeText(context, "REGS DE ALIMENTOS presionado", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.offset(x = screenWith / 2 - screenWith / 6, y = -screenWith / 28)
        ) {
            Icon(
                painter = painterResource(R.drawable.button1),
                contentDescription = null
            )
            Text(text = " REGIS DE ALIMENTOS", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    MainMenu()
}