package com.example.parcial2componentes.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearViajesScreen(
    onVolver: () -> Unit = {},
    onViajeCreado: () -> Unit = {}
) {
    var nombreViaje by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    val backgroundColor = Color(0xFF11152B)
    val cardColor = Color(0xFF1D2340)
    val accentColor = Color(0xFF9B83FF)
    val inputBgColor = Color(0xFF161B33)
    val secondaryText = Color(0xFFB7BCD2)

    val repository = remember { ViajesRepository() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun formatDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(millis))
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let { millis ->
                        fechaInicio = formatDate(millis)
                    }
                    showStartDatePicker = false
                }) {
                    Text("Aceptar", color = accentColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancelar", color = secondaryText)
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let { millis ->
                        fechaFin = formatDate(millis)
                    }
                    showEndDatePicker = false
                }) {
                    Text("Aceptar", color = accentColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancelar", color = secondaryText)
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Crear Viaje",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Datos del Viaje",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = nombreViaje,
                    onValueChange = { nombreViaje = it },
                    label = { Text("Nombre del viaje", color = secondaryText) },
                    placeholder = { Text("Ej. Vacaciones de Verano", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = inputBgColor,
                        unfocusedContainerColor = inputBgColor,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = secondaryText,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = destino,
                    onValueChange = { destino = it },
                    label = { Text("Destino", color = secondaryText) },
                    placeholder = { Text("Ej. Cartagena, Colombia", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = inputBgColor,
                        unfocusedContainerColor = inputBgColor,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = secondaryText,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fechaInicio,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Fecha de inicio", color = secondaryText) },
                        placeholder = { Text("AAAA-MM-DD", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showStartDatePicker = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = accentColor)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = inputBgColor,
                            disabledBorderColor = secondaryText,
                            disabledTextColor = Color.White,
                            disabledLabelColor = secondaryText,
                            disabledPlaceholderColor = Color.Gray
                        )
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showStartDatePicker = true }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fechaFin,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Fecha de fin", color = secondaryText) },
                        placeholder = { Text("AAAA-MM-DD", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showEndDatePicker = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = accentColor)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = inputBgColor,
                            disabledBorderColor = secondaryText,
                            disabledTextColor = Color.White,
                            disabledLabelColor = secondaryText,
                            disabledPlaceholderColor = Color.Gray
                        )
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showEndDatePicker = true }
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = errorMessage ?: "", color = Color(0xFFFF6B6B), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        if (nombreViaje.isBlank() || destino.isBlank()) {
                            errorMessage = "El nombre y el destino son obligatorios"
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            val nuevoViaje = Viaje(
                                name = nombreViaje,
                                destination = destino,
                                startDate = fechaInicio,
                                endDate = fechaFin
                            )
                            val result = repository.crearViaje(nuevoViaje)
                            isLoading = false
                            result.onSuccess {
                                onViajeCreado()
                            }.onFailure { err ->
                                errorMessage = err.localizedMessage ?: "Error al guardar el viaje"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Guardar Viaje",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, secondaryText)
                ) {
                    Text(
                        text = "Cancelar",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun crearviajes(
    onVolver: () -> Unit = {},
    onViajeCreado: () -> Unit = {}
) {
    CrearViajesScreen(onVolver = onVolver, onViajeCreado = onViajeCreado)
}
