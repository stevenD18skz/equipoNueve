package com.example.dogapp.data.remote.dto

// Importa la anotación SerializedName para mapear nombres de campos JSON a propiedades de Kotlin
import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que representa la respuesta de la API al solicitar una imagen de perro.
 * Utiliza anotaciones para mapear los campos JSON a propiedades de Kotlin.
 */
data class DogImageResponse(
    @SerializedName("message") // El campo "message" del JSON contiene la URL de la imagen
    val imageUrl: String, // Contendrá la URL de la imagen

    @SerializedName("status") // El campo "status" indica el estado de la respuesta (ej: "success")
    val status: String
)
