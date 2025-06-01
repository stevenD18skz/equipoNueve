package com.example.dogapp.data.remote

import com.example.dogapp.model.BreedResponse
import com.example.dogapp.data.remote.dto.DogImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogCeoApiService {

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedResponse>

    @GET("breed/{breed_name}/images/random")
    suspend fun getRandomImageByBreed(
        @Path("breed_name") breedName: String // Reemplaza {breed_name} en la URL
    ): Response<DogImageResponse>
}
