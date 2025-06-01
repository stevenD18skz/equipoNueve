package com.example.dogapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DogImageResponse(
    @SerializedName("message")
    val imageUrl: String, // Contendrá la URL de la imagen

    @SerializedName("status")
    val status: String
)
