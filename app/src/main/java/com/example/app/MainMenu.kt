package com.example.app

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    scanner: () -> Unit = {},
    ayunoInter: () -> Unit = {},
    contador: () -> Unit = {},
    imc: () -> Unit = {},
    calen: () -> Unit = {},
    alim: () -> Unit = {},
    chat: () -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWith = configuration.screenWidthDp.dp

    val ArcColor = Color(0xFFB6CBB0)
    val colorIconos = Color(0xFF6a815b)
    val imagen = ImageBitmap.imageResource(R.drawable.logo)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            // Logo y arco

            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxWidth(),
                onDraw = {
                    drawImage(
                        image = imagen,
                        topLeft = Offset(
                            x = -100f,
                            y = 195f
                        )
                    )
                    val arcSize = Size(screenHeight.value, screenHeight.value)

                    val posX = -409f
                    val posY = 45f

                    drawArc(
                        color = ArcColor,
                        startAngle = 270f,
                        sweepAngle = 180f,
                        useCenter = false,
                        size = arcSize,
                        style = Stroke(width = 40f),
                        topLeft = Offset(x = posX, y = posY)
                    )
                }
            )
            TextButton(
                onClick = { ayunoInter() },
                modifier = Modifier.offset(x = screenWith / 4 - screenWith / 6, y = -screenWith / 4f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "AYUNO INTERMITENTE", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }

            TextButton(
                onClick = { contador() },
                modifier = Modifier.offset(x = screenWith / 2.5f - screenWith / 6, y = -screenWith / 4.5f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "CONTADOR", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }

            TextButton(
                onClick = {
                    scanner()
                    Toast.makeText(context, "SCANER presionado", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.offset(x = screenWith / 1.75f - screenWith / 6, y = -screenWith / 7)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "SCANER", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }

            TextButton(
                onClick = {
                    Toast.makeText(context, "IMC precionado", Toast.LENGTH_SHORT).show()
                    imc()
                },
                modifier = Modifier.offset(x = screenWith / 2, y = -screenWith / 15)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "IMC", color = Color.White,fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }

            TextButton(
                onClick = { calen() },
                modifier = Modifier.offset(x = screenWith / 2 - screenWith / 9, y = -screenWith / -70)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "CALENDARIO", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }

            TextButton(
                onClick = { alim() },
                modifier = Modifier.offset(x = screenWith / 3 - screenWith / 9, y = -screenWith / -10)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "REGIS DE ALIMENTOS", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }

            TextButton(
                onClick = { chat() },
                modifier = Modifier.offset(x = screenWith / 4 - screenWith / 6, y = -screenWith / -6)
            ) {
                Icon(
                    painter = painterResource(R.drawable.button1),
                    contentDescription = null,
                    tint = colorIconos
                )
                Text(text = "CHAT BOT", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainMenu()
}
