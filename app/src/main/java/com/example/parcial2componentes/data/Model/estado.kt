package com.example.parcial2componentes.data.Model

import com.google.gson.annotations.SerializedName

data class Estado(
    @SerializedName("estado") val estado: String = ""
)

typealias estado = Estado
