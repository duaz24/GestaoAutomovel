package com.example.gestaoautomovel

data class Gasto(
    val id: String = "",
    val tipo: String = "",
    val valor: Double = 0.0,
    val litros: Double = 0.0,
    val kmAtuais: Double = 0.0
)