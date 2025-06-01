package com.example.dogapp.model

data class BreedResponse(
    val message: Map<String, List<String>>,
    val status: String
)