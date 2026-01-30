package br.com.devjhonl.boltchallenge.dto

import java.math.BigDecimal

data class GeradorResponseDTO(
    val nome: String,
    val codigo: String,
    val tipo: String,
    val potencia: BigDecimal
)