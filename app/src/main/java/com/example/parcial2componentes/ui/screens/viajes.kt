package com.example.parcial2componentes.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun viajes(
    onNavigateToGame: () -> Unit = {},
    onNavigateToJoin: () -> Unit = {},
    onNavigateToAvailableGames: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val backgroundColor = Color(0xFF11152B)
    val cardColor = Color(0xFF1D2340)
    val accentColor = Color(0xFF9B83FF)
    val secondaryText = Color(0xFFB7BCD2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mis Viajes",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

                OutlinedButton(
                    onClick = onNavigateToAvailableGames,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(
                        1.dp,
                        secondaryText
                    )
                ) {
                    Text(
                        text = "crear Viaje",
                        fontSize = 16.sp
                    )
                }
            }
        }

