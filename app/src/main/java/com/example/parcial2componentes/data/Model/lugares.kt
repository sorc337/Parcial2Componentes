package com.example.parcial2componentes.data.Model

import com.google.gson.annotations.SerializedName

data class Lugar(
    @SerializedName("id") val id: String? = null,
    @SerializedName(value = "tripId", alternate = ["trip_id"]) val tripId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName(value = "categoryId", alternate = ["category_id"]) val categoryId: Int = 0,
    @SerializedName(value = "tentativeDate", alternate = ["tentative_date"]) val tentativeDate: String = "",
    @SerializedName("category") val category: Categoria? = null
)

typealias lugares = Lugar
