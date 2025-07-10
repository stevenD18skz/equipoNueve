package com.example.dogapp.data.remote

// Importa las dependencias necesarias para la configuración de Retrofit y HTTP
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton que configura y proporciona una instancia de Retrofit
 * para consumir la API de Dog CEO.
 */
object RetrofitClient {

    // URL base de la API pública de Dog CEO
    private const val BASE_URL = "https://dog.ceo/api/"

    // Interceptor de logging para mostrar en consola las solicitudes y respuestas HTTP (útil para depuración)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Muestra el cuerpo completo de las peticiones/respuestas
    }

    // Cliente HTTP personalizado que incluye el interceptor de logging
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Agrega el interceptor al cliente
        .build()

    // Instancia de Retrofit configurada con la URL base, el cliente HTTP y el convertidor Gson
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Establece la URL base para todas las peticiones
            .client(httpClient) // Usa el cliente HTTP personalizado
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON automáticamente a objetos Kotlin
            .build()
    }

    // Provee una instancia del servicio de API de Dog CEO, lista para usarse en ViewModels y Repositorios
    val dogCeoApiService: DogCeoApiService by lazy {
        retrofit.create(DogCeoApiService::class.java)
    }
}
