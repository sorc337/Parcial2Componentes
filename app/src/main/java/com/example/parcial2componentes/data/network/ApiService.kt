package com.example.parcial2componentes.data.network

import com.example.parcial2componentes.data.Model.Categoria
import com.example.parcial2componentes.data.Model.Lugar
import com.example.parcial2componentes.data.Model.Viaje
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/trips")
    suspend fun getViajes(): Response<List<Viaje>>

    @POST("api/trips")
    suspend fun crearViaje(@Body viaje: Viaje): Response<Viaje>

    @GET("api/trips/{id}")
    suspend fun getDetalleViaje(@Path("id") id: String): Response<Viaje>

    @POST("api/places")
    suspend fun registrarLugar(@Body lugar: Lugar): Response<Lugar>

    @GET("api/places")
    suspend fun getLugaresPorViaje(@Query("tripId") tripId: String): Response<List<Lugar>>

    @GET("api/categories")
    suspend fun getCategorias(): Response<List<Categoria>>
}
