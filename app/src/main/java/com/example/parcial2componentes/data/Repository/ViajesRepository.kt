package com.example.parcial2componentes.data.Repository

import com.example.parcial2componentes.data.Model.Categoria
import com.example.parcial2componentes.data.Model.Lugar
import com.example.parcial2componentes.data.Model.Viaje
import com.example.parcial2componentes.data.network.ApiService
import com.example.parcial2componentes.data.network.RetrofitClient

class ViajesRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {
    suspend fun obtenerViajes(): Result<List<Viaje>> {
        return try {
            val response = apiService.getViajes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener viajes: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearViaje(viaje: Viaje): Result<Viaje> {
        return try {
            val response = apiService.crearViaje(viaje)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear viaje: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerDetalleViaje(id: String): Result<Viaje> {
        return try {
            val response = apiService.getDetalleViaje(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener detalle: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarLugar(lugar: Lugar): Result<Lugar> {
        return try {
            val response = apiService.registrarLugar(lugar)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al registrar lugar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerLugaresPorViaje(tripId: String): Result<List<Lugar>> {
        return try {
            val response = apiService.getLugaresPorViaje(tripId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener lugares: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerCategorias(): Result<List<Categoria>> {
        return try {
            val response = apiService.getCategorias()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener categorías: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
