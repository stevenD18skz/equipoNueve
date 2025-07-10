package com.example.dogapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una cita en la base de datos local (Room).
 * Cada propiedad corresponde a una columna de la tabla "appointments".
 */
@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) // Clave primaria autoincremental
    val id: Int = 0,
    val petName: String,             // Nombre de la mascota
    val breed: String,               // Raza de la mascota
    val ownerName: String,           // Nombre del dueño
    val ownerPhone: String,          // Teléfono del dueño
    val symptoms: String,            // Síntomas reportados
    val petImageUrl: String? = null  // URL de la imagen de la mascota (opcional)
)