package com.example.dogapp.model

/**
 * Modelo de datos para representar una cita veterinaria en la capa de presentación.
 * Este modelo se usa para mostrar y transferir información de una cita entre la UI y el ViewModel.
 *
 * @property id Identificador único de la cita (autoincremental).
 * @property petName Nombre de la mascota asociada a la cita.
 * @property breed Raza de la mascota.
 * @property ownerName Nombre del dueño de la mascota.
 * @property ownerPhone Teléfono de contacto del dueño.
 * @property symptoms Síntomas o motivo de la cita.
 * @property petImageUrl URL de la imagen de la mascota (opcional).
 */
data class Appointment(
    val id: Int,                // ID único de la cita
    val petName: String,        // Nombre de la mascota
    val breed: String,          // Raza de la mascota
    val ownerName: String,      // Nombre del dueño
    val ownerPhone: String,     // Teléfono del dueño
    val symptoms: String,       // Síntomas o motivo de la cita
    val petImageUrl: String? = null // URL de la imagen de la mascota (opcional)
)

