package com.example.dogapp.data.remote

// Importa el modelo de respuesta para razas de perros
import com.example.dogapp.model.BreedResponse
// Importa el modelo de respuesta para imágenes de perros
import com.example.dogapp.data.remote.dto.DogImageResponse
// Importa la clase Response de Retrofit para manejar respuestas HTTP
import retrofit2.Response
// Importa las anotaciones necesarias para definir endpoints en Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz que define los endpoints de la API pública Dog CEO para obtener razas e imágenes de perros.
 * Se utiliza con Retrofit para consumir servicios REST de forma sencilla y tipada.
 */
interface DogCeoApiService {

    /**
     * Obtiene la lista de todas las razas de perros disponibles en la API.
     * Endpoint: GET https://dog.ceo/api/breeds/list/all
     * @return Response<BreedResponse> con la estructura de razas.
     */
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedResponse>

    /**
     * Obtiene una imagen aleatoria de un perro de la raza especificada.
     * Endpoint: GET https://dog.ceo/api/breed/{breed_name}/images/random
     * @param breedName Nombre de la raza a buscar (se reemplaza en la URL)
     * @return Response<DogImageResponse> con la URL de la imagen.
     */
    @GET("breed/{breed_name}/images/random")
    suspend fun getRandomImageByBreed(
        @Path("breed_name") breedName: String // Reemplaza {breed_name} en la URL
    ): Response<DogImageResponse>
}
