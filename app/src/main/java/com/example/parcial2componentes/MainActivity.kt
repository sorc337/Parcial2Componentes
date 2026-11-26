
package com.example.parcial2componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pantalla Principal",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onNavigateToViajes) {
                Text(text = "Ir a Viajes")
            }
        }
    }
}
