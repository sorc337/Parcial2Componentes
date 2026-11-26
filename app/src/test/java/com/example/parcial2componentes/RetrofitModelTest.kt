package com.example.parcial2componentes

import com.example.parcial2componentes.data.Model.Categoria
import com.example.parcial2componentes.data.Model.Lugar
import com.example.parcial2componentes.data.Model.Viaje
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitModelTest {

    private val gson = Gson()

    @Test
    fun testViajeJsonDeserialization() {
        val jsonFromPython = """
            {
                "id": "1",
                "name": "Vacaciones Cartagena",
                "destination": "Cartagena",
                "startDate": "2026-06-01",
                "endDate": "2026-06-10"
            }
        """.trimIndent()

        val viaje = gson.fromJson(jsonFromPython, Viaje::class.java)

        assertNotNull(viaje)
        assertEquals("1", viaje.id)
        assertEquals("Vacaciones Cartagena", viaje.name)
        assertEquals("Cartagena", viaje.destination)
        assertEquals("2026-06-01", viaje.startDate)
        assertEquals("2026-06-10", viaje.endDate)
    }

    @Test
    fun testLugarJsonDeserialization() {
        val jsonFromPython = """
            {
                "id": "101",
                "tripId": "1",
                "name": "Castillo San Felipe",
                "description": "Recorrido historico",
                "city": "Cartagena",
                "categoryId": 1,
                "tentativeDate": "2026-06-03"
            }
        """.trimIndent()

        val lugar = gson.fromJson(jsonFromPython, Lugar::class.java)

        assertNotNull(lugar)
        assertEquals("101", lugar.id)
        assertEquals("1", lugar.tripId)
        assertEquals("Castillo San Felipe", lugar.name)
        assertEquals(1, lugar.categoryId)
        assertEquals("2026-06-03", lugar.tentativeDate)
    }

    @Test
    fun testCategoriaJsonDeserialization() {
        val jsonFromPython = """
            {
                "id": 1,
                "name": "Historico",
                "description": "Sitios historicos"
            }
        """.trimIndent()

        val categoria = gson.fromJson(jsonFromPython, Categoria::class.java)

        assertNotNull(categoria)
        assertEquals(1, categoria.id)
        assertEquals("Historico", categoria.name)
        assertEquals("Sitios historicos", categoria.description)
    }
}
