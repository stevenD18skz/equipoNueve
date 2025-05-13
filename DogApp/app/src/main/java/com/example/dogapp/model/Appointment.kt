package com.example.dogapp.model // Ajusta tu package name

// Clase simple para representar los datos de la cita (sin anotaciones de Room)
data class Appointment(
    val id: Int,
    val petName: String,
    val breed: String, // Aunque no la usemos mucho visualmente en el item aún
    val ownerName: String, // No se muestra en la lista, pero puede ser útil para el detalle
    val ownerPhone: String, // No se muestra en la lista
    val symptoms: String,
    val petImageUrl: String? = null // Mantenemos esto para la estructura del item
)