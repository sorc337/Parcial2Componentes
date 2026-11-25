package com.example.parcial2componentes.data.Model

import com.google.gson.annotations.SerializedName

data class Viaje(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("destination") val destination: String = "",
    @SerializedName(value = "startDate", alternate = ["start_date", "starDate"]) val startDate: String = "",
    @SerializedName(value = "endDate", alternate = ["end_date", "endate"]) val endDate: String = "",
    @SerializedName(value = "places", alternate = ["lugares"]) val places: List<Lugar>? = null
)

typealias viaje = Viaje
