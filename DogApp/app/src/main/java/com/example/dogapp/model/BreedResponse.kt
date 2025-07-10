package com.example.dogapp.model

/**
 * Modelo de datos para mapear la respuesta de la API de razas de perros.
 * @property message Mapa donde la clave es el nombre de la raza y el valor es una lista de subrazas.
 * @property status Estado de la respuesta (por ejemplo, "success").
 */
data class BreedResponse(
    val message: Map<String, List<String>>, // Razas y subrazas
    val status: String                     // Estado de la respuesta
)