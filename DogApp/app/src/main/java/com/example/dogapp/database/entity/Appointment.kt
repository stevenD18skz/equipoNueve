package com.example.dogapp.database.entity // Ajusta tu package name

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petName: String,
    val breed: String,
    val ownerName: String,
    val ownerPhone: String,
    val symptoms: String,
    val petImageUrl: String? = null
)