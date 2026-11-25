package com.example.parcial2componentes.data.Model

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("id") val id: Int = 0,
    @SerializedName(value = "name", alternate = ["category"]) val name: String = "",
    @SerializedName("description") val description: String = ""
)

typealias categorias = Categoria
