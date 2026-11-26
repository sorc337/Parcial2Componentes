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
import com.example.parcial2componentes.data.Model.Lugar
import com.example.parcial2componentes.data.Model.Viaje
import com.example.parcial2componentes.data.Repository.ViajesRepository
import kotlinx.coroutines.launch

@Composable
fun ConsultarDetalleViajeScreen(
    tripId: String,
    onVolver: () -> Unit = {},
    onNavigateToAddLugar: (String) -> Unit = {}
) {
    val backgroundColor = Color(0xFF11152B)
    val cardColor = Color(0xFF1D2340)
    val accentColor = Color(0xFF9B83FF)
    val secondaryText = Color(0xFFB7BCD2)

    val repository = remember { ViajesRepository() }
    var viaje by remember { mutableStateOf<Viaje?>(null) }
    var lugares by remember { mutableStateOf<List<Lugar>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    fun cargarDatos() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            val detalleResult = repository.obtenerDetalleViaje(tripId)
            detalleResult.onSuccess { v ->
                viaje = v
                val lugaresList = v.places ?: emptyList()
                if (lugaresList.isNotEmpty()) {
                    lugares = lugaresList
                } else {
                    val lugaresResult = repository.obtenerLugaresPorViaje(tripId)
                    lugaresResult.onSuccess { l ->
                        lugares = l
                    }.onFailure {
                        lugares = emptyList()
                    }
                }
                isLoading = false
            }.onFailure { error ->
                val lugaresResult = repository.obtenerLugaresPorViaje(tripId)
                lugaresResult.onSuccess { l ->
                    lugares = l
                }
                val viajesResult = repository.obtenerViajes()
                viajesResult.onSuccess { lista ->
                    val found = lista.find { it.id == tripId }
                    if (found != null) {
                        viaje = found
                        if (viaje?.places != null && viaje!!.places!!.isNotEmpty()) {
                            lugares = viaje!!.places!!
                        }
                    } else {
                        errorMessage = error.localizedMessage ?: "Error al cargar detalle del viaje"
                    }
                }.onFailure {
                    errorMessage = error.localizedMessage ?: "Error al cargar detalle del viaje"
                }
                isLoading = false
            }
        }
    }

    LaunchedEffect(tripId) {
        cargarDatos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Detalles del Viaje",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = onVolver,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, secondaryText)
            ) {
                Text("Volver", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = accentColor)
                }
            }
            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage ?: "Error",
                        color = Color(0xFFFF6B6B),
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = BorderStroke(1.dp, Color(0xFF2B3254))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = viaje?.name ?: "Viaje sin nombre",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Destino: ${viaje?.destination ?: "No especificado"}",
                            fontSize = 16.sp,
                            color = accentColor,
                            fontWeight = FontWeight.Medium
                        )
                        if (!viaje?.startDate.isNullOrEmpty() || !viaje?.endDate.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Fechas: ${viaje?.startDate} al ${viaje?.endDate}",
                                fontSize = 14.sp,
                                color = secondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lugares a Visitar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (lugares.isEmpty()) {
                            Text(
                                text = "No hay lugares agregados para este viaje",
                                color = secondaryText,
                                fontSize = 15.sp
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(lugares) { lugar ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B33)),
                                        border = BorderStroke(1.dp, Color(0xFF2B3254))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp)
                                        ) {
                                            Text(
                                                text = lugar.name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            if (lugar.city.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Ciudad: ${lugar.city}",
                                                    fontSize = 14.sp,
                                                    color = accentColor
                                                )
                                            }
                                            if (lugar.description.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = lugar.description,
                                                    fontSize = 13.sp,
                                                    color = secondaryText
                                                )
                                            }
                                            if (lugar.tentativeDate.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Fecha tentativa: ${lugar.tentativeDate}",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFFB7BCD2)
                                                )
                                            }
                                            if (lugar.category != null && lugar.category.name.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Categoría: ${lugar.category.name}",
                                                    fontSize = 12.sp,
                                                    color = accentColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onNavigateToAddLugar(tripId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text(
                        text = "Agregar Lugar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun consultarDetalleViaje(
    tripId: String = "",
    onVolver: () -> Unit = {},
    onNavigateToAddLugar: (String) -> Unit = {}
) {
    ConsultarDetalleViajeScreen(
        tripId = tripId,
        onVolver = onVolver,
        onNavigateToAddLugar = onNavigateToAddLugar
    )
}
