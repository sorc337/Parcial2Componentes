package com.example.parcial2componentes

import com.example.parcial2componentes.data.Model.Lugar
import com.example.parcial2componentes.data.Model.Viaje
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionYCalculoTest {

    // --- Validaciones de Viajes ---
    private fun esViajeValido(viaje: Viaje): Boolean {
        if (viaje.name.isBlank() || viaje.destination.isBlank() || viaje.startDate.isBlank() || viaje.endDate.isBlank()) {
            return false
        }
        return viaje.startDate <= viaje.endDate
    }

    private fun esFechaLugarValida(lugar: Lugar, viaje: Viaje): Boolean {
        if (lugar.name.isBlank() || lugar.tentativeDate.isBlank() || lugar.categoryId <= 0) {
            return false
        }
        return lugar.tentativeDate >= viaje.startDate && lugar.tentativeDate <= viaje.endDate
    }

    private fun validateExpenseAmount(amount: Double): Boolean {
        return amount > 0.0
    }

    @Test
    fun testViajeValido_Correcto() {
        val viaje = Viaje(
            name = "Vacaciones de Verano",
            destination = "Cartagena",
            startDate = "2026-06-01",
            endDate = "2026-06-15"
        )
        assertTrue("El viaje con datos válidos debe ser aceptado", esViajeValido(viaje))
    }

    @Test
    fun testViajeInvalido_CamposVacios() {
        val viajeSinNombre = Viaje(
            name = "",
            destination = "Cartagena",
            startDate = "2026-06-01",
            endDate = "2026-06-15"
        )
        assertFalse("El viaje sin nombre debe ser rechazado", esViajeValido(viajeSinNombre))
    }

    @Test
    fun testViajeInvalido_FechaFinAnteriorAInicio() {
        val viajeFechasInvalidas = Viaje(
            name = "Viaje Imposible",
            destination = "Medellín",
            startDate = "2026-06-15",
            endDate = "2026-06-01"
        )
        assertFalse("Fecha de fin anterior a la fecha de inicio debe ser rechazada", esViajeValido(viajeFechasInvalidas))
    }

    @Test
    fun testFechaLugarDentroDelRangoDelViaje() {
        val viaje = Viaje(startDate = "2026-06-01", endDate = "2026-06-15")

        val lugarValido = Lugar(
            name = "Castillo San Felipe",
            categoryId = 1,
            tentativeDate = "2026-06-05"
        )
        assertTrue("Fecha del lugar dentro del rango debe ser aceptada", esFechaLugarValida(lugarValido, viaje))

        val lugarFechaFuera = Lugar(
            name = "Playa Blanca",
            categoryId = 1,
            tentativeDate = "2026-07-01"
        )
        assertFalse("Fecha del lugar fuera del rango debe ser rechazada", esFechaLugarValida(lugarFechaFuera, viaje))
    }

    @Test
    fun `gasto con monto cero o negativo es invalido`() {
        assertFalse(validateExpenseAmount(0.0))
        assertFalse(validateExpenseAmount(-10.5))
        assertTrue(validateExpenseAmount(50.0))
    }

    // --- Cálculo de Totales y Aislamiento ---
    data class GastoTestModel(
        val id: String,
        val tripId: String,
        val description: String,
        val amount: Double
    )

    private val sampleExpenses = listOf(
        GastoTestModel("1", "trip_1", "Hotel", 250.0),
        GastoTestModel("2", "trip_1", "Comida", 45.5),
        GastoTestModel("3", "trip_2", "Transporte", 60.0),
        GastoTestModel("4", "trip_1", "Tour", 80.0)
    )

    @Test
    fun `calcula suma total de gastos asociados a un viaje correctamente`() {
        val targetTripId = "trip_1"
        val total = calculateTotalExpenses(sampleExpenses, targetTripId)
        assertEquals(375.5, total, 0.001)
    }

    @Test
    fun `gastos de otros viajes no se mezclan en el calculo`() {
        val targetTripId = "trip_2"
        val total = calculateTotalExpenses(sampleExpenses, targetTripId)
        assertEquals(60.0, total, 0.001)
    }

    @Test
    fun `total es cero si el viaje no tiene gastos registrados`() {
        val targetTripId = "trip_3"
        val total = calculateTotalExpenses(sampleExpenses, targetTripId)
        assertEquals(0.0, total, 0.001)
    }

    private fun calculateTotalExpenses(expenses: List<GastoTestModel>, tripId: String): Double {
        return expenses.filter { it.tripId == tripId }.sumOf { it.amount }
    }
}
