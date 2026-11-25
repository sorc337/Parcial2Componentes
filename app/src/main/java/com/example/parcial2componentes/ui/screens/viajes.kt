package com.example.parcial2componentes.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcial2componentes.data.Model.Viaje
import com.example.parcial2componentes.data.Repository.ViajesRepository

@Composable
fun viajes(
    onNavigateToAvailableGames: () -> Unit = {}
) {
    val backgroundColor = Color(0xFF11152B)
    val cardColor = Color(0xFF1D2340)
    val accentColor = Color(0xFF9B83FF)
    val secondaryText = Color(0xFFB7BCD2)

    val repository = remember { ViajesRepository() }
    var listaViajes by remember { mutableStateOf<List<Viaje>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        val result = repository.obtenerViajes()
        result.onSuccess { viajes ->
            listaViajes = viajes
            isLoading = false
        }.onFailure { error ->
            errorMessage = error.localizedMessage ?: "Error al cargar los viajes"
            isLoading = false
        }
    }

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

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(color = accentColor)
                    }
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Error desconocido",
                            color = Color(0xFFFF6B6B),
                            fontSize = 16.sp
                        )
                    }
                    listaViajes.isEmpty() -> {
                        Text(
                            text = "No hay viajes registrados",
                            color = secondaryText,
                            fontSize = 16.sp
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(listaViajes) { viaje ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161B33)),
                                    border = BorderStroke(1.dp, Color(0xFF2B3254))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = viaje.name,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Destino: ${viaje.destination}",
                                            fontSize = 15.sp,
                                            color = accentColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                        if (viaje.startDate.isNotEmpty() || viaje.endDate.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "${viaje.startDate} - ${viaje.endDate}",
                                                fontSize = 13.sp,
                                                color = secondaryText
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                text = "Crear Viaje",
                fontSize = 16.sp
            )
        }
    }
}
