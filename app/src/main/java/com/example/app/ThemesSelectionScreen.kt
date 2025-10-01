package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class AppTheme(
    val name: String,
    val primary: Color,
    val secondary: Color,
    val background: Color
)

@Composable
fun ThemeSelectorScreen(
    onThemeSelected: (AppTheme) -> Unit,
    onBack: () -> Unit
) {
    val lightThemes = listOf(
        AppTheme("Azul Pastel", Color(0xFF5C7FE2), Color(0xFFAEC6CF), Color(0xFFF0F4FF)),
        AppTheme("Rosa Pastel", Color(0xFFE95E86), Color(0xFFFFB6C1), Color(0xFFFFF0F3)),
        AppTheme("Naranja Pastel", Color(0xFFF9884E), Color(0xFFFFDAB9), Color(0xFFFFF3EE)),
    )
    val darkThemes = listOf(
        AppTheme("Morado", Color(0xFFC0A0FF), Color(0xFFC8BFEA), Color(0xFF141218)),
        AppTheme("Azul Marino", Color(0xFFA5C8FF), Color(0xFF4169E1), Color(0xFF10141A)),
        AppTheme("Naranja Otoño", Color(0xFFE59C47), Color(0xFFD2691E), Color(0xFF1A130B)),
    )

    var selectedTheme by remember { mutableStateOf<AppTheme?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Temas Claros", style = MaterialTheme.typography.titleMedium)
        ThemeRow(lightThemes, selectedTheme) {
            selectedTheme = it
            onThemeSelected(it)
        }

        Text("Temas Oscuros", style = MaterialTheme.typography.titleMedium)
        ThemeRow(darkThemes, selectedTheme) {
            selectedTheme = it
            onThemeSelected(it)
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "← Volver",
            modifier = Modifier
                .clickable { onBack() }
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ThemeRow(
    themes: List<AppTheme>,
    selectedTheme: AppTheme?,
    onThemeClick: (AppTheme) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        themes.forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeClick(theme) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(theme.primary)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(theme.background)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(theme.secondary)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = theme.name,
                    color = if (selectedTheme == theme) MaterialTheme.colorScheme.primary else Color.Unspecified
                )
            }
        }
    }
}
