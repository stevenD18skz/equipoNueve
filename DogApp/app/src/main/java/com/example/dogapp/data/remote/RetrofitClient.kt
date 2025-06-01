package com.example.dogapp.data.remote // Ajusta tu package name

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // O MoshiConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://dog.ceo/api/"

    // Logging Interceptor para ver las solicitudes y respuestas (útil para depuración)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Añadir el interceptor aquí
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Usar el cliente OkHttp con el interceptor
            .addConverterFactory(GsonConverterFactory.create()) // O MoshiConverterFactory.create()
            .build()
    }

    val dogCeoApiService: DogCeoApiService by lazy {
        retrofit.create(DogCeoApiService::class.java)
    }
}
