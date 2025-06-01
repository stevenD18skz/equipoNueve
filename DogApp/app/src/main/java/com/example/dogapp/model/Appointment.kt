package com.example.dogapp.model

data class Appointment(
    val id: Int,
    val petName: String,
    val breed: String,
    val ownerName: String,
    val ownerPhone: String,
    val symptoms: String,
    val petImageUrl: String? = null
)

