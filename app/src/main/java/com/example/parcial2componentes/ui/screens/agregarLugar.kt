
package com.example.parcial2componentes.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcial2componentes.data.Model.Categoria
import com.example.parcial2componentes.data.Model.Lugar
import com.example.parcial2componentes.data.Repository.ViajesRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val Fondo = Color(0xFF11152B)
private val Tarjeta = Color(0xFF1D2340)
private val Morado = Color(0xFF9B83FF)
private val TextoSecundario = Color(0xFFB7BCD2)

@Composable
fun AgregarLugarScreen(
    tripId: String,
    onVolver: () -> Unit,
    onLugarRegistrado: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { ViajesRepository() }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
    var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }
    var menuAbierto by remember { mutableStateOf(false) }

    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    var cargando by remember { mutableStateOf(false) }
    var cargandoDatos by remember { mutableStateOf(true) }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }

    // Consultar las categorías y las fechas del viaje seleccionado.
    LaunchedEffect(tripId) {
        cargandoDatos = true
        mensaje = ""

        repository.obtenerCategorias()
            .onSuccess {
                categorias = it
                if (it.isEmpty()) {
                    mensaje = "No hay categorías disponibles"
                    esError = true
                }
            }
            .onFailure {
                mensaje = "No se pudieron cargar las categorías"
                esError = true
            }

        repository.obtenerDetalleViaje(tripId)
            .onSuccess {
                fechaInicio = it.startDate.take(10)
                fechaFin = it.endDate.take(10)
            }
            .onFailure {
                mensaje = "No se pudo consultar la información del viaje"
                esError = true
            }

        cargandoDatos = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Agregar lugar",
            color = Color.White,
            fontSize = 26.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Registra un lugar que deseas visitar durante tu viaje.",
            color = TextoSecundario,
            fontSize = 14.sp
        )

        // Formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Tarjeta
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Información del lugar",
                    color = Morado,
                    fontSize = 18.sp
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del lugar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando,
                    colors = camposColores()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    enabled = !cargando,
                    colors = camposColores()
                )

                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = { Text("Ciudad") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando,
                    colors = camposColores()
                )

                // Selección de categoría
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { menuAbierto = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !cargando,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            categoriaSeleccionada?.name
                                ?: "Seleccionar categoría"
                        )
                    }

                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false },
                        modifier = Modifier.background(Tarjeta)
                    ) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        categoria.name,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    categoriaSeleccionada = categoria
                                    menuAbierto = false
                                }
                            )
                        }
                    }
                }

                // Selección de fecha tentativa
                OutlinedButton(
                    onClick = {
                        val calendario = Calendar.getInstance()

                        val selector = DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                fecha = String.format(
                                    Locale.US,
                                    "%04d-%02d-%02d",
                                    year,
                                    month + 1,
                                    day
                                )
                            },
                            calendario.get(Calendar.YEAR),
                            calendario.get(Calendar.MONTH),
                            calendario.get(Calendar.DAY_OF_MONTH)
                        )

                        // Limitar el calendario al período del viaje.
                        val formato = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.US
                        ).apply {
                            isLenient = false
                        }

                        try {
                            val inicio = formato.parse(fechaInicio)
                            val fin = formato.parse(fechaFin)

                            if (inicio != null && fin != null) {
                                selector.datePicker.minDate = inicio.time
                                selector.datePicker.maxDate = fin.time
                            }
                        } catch (_: Exception) {
                            // La validación también se realiza al guardar.
                        }

                        selector.show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando && fechaInicio.isNotBlank() &&
                            fechaFin.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (fecha.isBlank()) {
                            "Seleccionar fecha tentativa"
                        } else {
                            "Fecha: $fecha"
                        }
                    )
                }

                if (fechaInicio.isNotBlank() && fechaFin.isNotBlank()) {
                    Text(
                        text = "Período del viaje: $fechaInicio al $fechaFin",
                        color = TextoSecundario,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                color = if (esError) {
                    Color(0xFFFF7777)
                } else {
                    Color(0xFF81C784)
                },
                fontSize = 14.sp
            )
        }

        Button(
            onClick = {
                when {
                    nombre.isBlank() ||
                            descripcion.isBlank() ||
                            ciudad.isBlank() ||
                            categoriaSeleccionada == null ||
                            fecha.isBlank() -> {
                        mensaje = "Completa todos los campos"
                        esError = true
                    }

                    fechaInicio.isBlank() || fechaFin.isBlank() -> {
                        mensaje = "No se pudieron obtener las fechas del viaje"
                        esError = true
                    }

                    !fechaValida(fecha, fechaInicio, fechaFin) -> {
                        mensaje = "La fecha debe estar dentro del período del viaje"
                        esError = true
                    }

                    else -> {
                        scope.launch {
                            cargando = true
                            mensaje = ""

                            val lugar = Lugar(
                                tripId = tripId,
                                name = nombre.trim(),
                                description = descripcion.trim(),
                                city = ciudad.trim(),
                                categoryId = categoriaSeleccionada!!.id,
                                tentativeDate = fecha
                            )

                            repository.registrarLugar(lugar)
                                .onSuccess {
                                    mensaje = "¡Lugar registrado correctamente!"
                                    esError = false
                                    onLugarRegistrado()
                                }
                                .onFailure {
                                    mensaje = it.message
                                        ?: "Error al registrar el lugar"
                                    esError = true
                                }

                            cargando = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !cargando && !cargandoDatos,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Morado,
                contentColor = Color.White
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("REGISTRAR LUGAR")
            }
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            )
        ) {
            Text("VOLVER AL VIAJE")
        }
    }
}

@Composable
private fun camposColores() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Morado,
    unfocusedBorderColor = TextoSecundario,
    focusedLabelColor = Morado,
    unfocusedLabelColor = TextoSecundario,
    cursorColor = Morado
)
private fun fechaValida(
    fecha: String,
    inicio: String,
    fin: String
): Boolean {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        isLenient = false
    }

    return try {
        val fechaLugar = formato.parse(fecha)
        val fechaInicio = formato.parse(inicio)
        val fechaFin = formato.parse(fin)

        fechaLugar != null &&
                fechaInicio != null &&
                fechaFin != null &&
                !fechaLugar.before(fechaInicio) &&
                !fechaLugar.after(fechaFin)
    } catch (_: Exception) {
        false
    }
}
