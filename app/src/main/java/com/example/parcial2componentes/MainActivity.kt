
package com.example.parcial2componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parcial2componentes.ui.screens.AgregarLugarScreen
import com.example.parcial2componentes.ui.screens.ConsultarDetalleViajeScreen
import com.example.parcial2componentes.ui.screens.CrearViajesScreen
import com.example.parcial2componentes.ui.screens.viajes
import com.example.parcial2componentes.ui.theme.Parcial2ComponentesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Parcial2ComponentesTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToViajes = {
                    navController.navigate("viajes")
                }
            )
        }

        composable("viajes") {
            viajes(
                onNavigateToAvailableGames = {
                    navController.navigate("crearviajes")
                },
                onNavigateToDetalle = { tripId ->
                    navController.navigate("consultardeDetalles/$tripId")
                }
            )
        }

        composable("crearviajes") {
            CrearViajesScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onViajeCreado = {
                    navController.popBackStack()
                }
            )
        }

        composable("consultardeDetalles/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""

            ConsultarDetalleViajeScreen(
                tripId = tripId,
                onVolver = {
                    navController.popBackStack()
                },
                onNavigateToAddLugar = { id ->
                    navController.navigate("agregarLugar/$id")
                }
            )
        }

        composable("agregarLugar/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""

            AgregarLugarScreen(
                tripId = tripId,
                onVolver = {
                    navController.popBackStack()
                },
                onLugarRegistrado = {
                    navController.navigate("consultardeDetalles/$tripId") {
                        popUpTo("consultardeDetalles/$tripId") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(onNavigateToViajes: () -> Unit) {
    val fondoColor = Color(0xFF11152B)
    val tarjetaColor = Color(0xFF1D2340)
    val moradoAccent = Color(0xFF9B83FF)
    val textoSecundario = Color(0xFFB7BCD2)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = fondoColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = tarjetaColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gestión de Viajes",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Organiza tus viajes, registra tus gastos, controla las fechas y gestiona cada destino de forma fácil y moderna.",
                        fontSize = 15.sp,
                        color = textoSecundario,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = onNavigateToViajes,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = moradoAccent)
                    ) {
                        Text(
                            text = "Ver Mis Viajes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B33))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Control de Gastos & Categorías",
                        fontSize = 14.sp,
                        color = textoSecundario,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
