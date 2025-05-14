package com.example.dogapp.data.remote

import retrofit2.Response
import retrofit2.http.GET
import com.example.dogapp.model.BreedResponse

interface DogCeoApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedResponse>
}