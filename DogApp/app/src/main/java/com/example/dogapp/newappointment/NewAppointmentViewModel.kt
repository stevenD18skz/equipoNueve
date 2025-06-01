package com.example.dogapp.newappointment

import android.app.Application
import androidx.lifecycle.*
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment as DbAppointment // Alias para evitar colisión
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.repository.AppointmentRepository // Necesitarás el repositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Helper function para combinar LiveData (si no la tienes en un archivo utils)
fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MutableLiveData<R>()
    this.observeForever {
        result.value = block(this.value, liveData.value)
    }
    liveData.observeForever {
        result.value = block(this.value, liveData.value)
    }
    return result
}

class NewAppointmentViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Repositorio para interactuar con la base de datos
    private val repository: AppointmentRepository

    // Campos del formulario
    val petName = MutableLiveData<String>("")
    val breed = MutableLiveData<String>("") // Este se llenará del AutoComplete
    val ownerName = MutableLiveData<String>("")
    val ownerPhone = MutableLiveData<String>("")
    val selectedSymptom = MutableLiveData<String>("")

    // Lista de razas que ahora vendrá de la API
    private val _breedList = MutableLiveData<List<String>>(emptyList())
    val breedList: LiveData<List<String>> = _breedList


    // Botón habilitado si los campos obligatorios no están vacíos
    val isSaveButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun validate() {
            value = !petName.value.isNullOrBlank() &&
                    !breed.value.isNullOrBlank() &&
                    !ownerName.value.isNullOrBlank() &&
                    !ownerPhone.value.isNullOrBlank()
        }
        addSource(petName)    { validate() }
        addSource(breed)      { validate() }
        addSource(ownerName)  { validate() }
        addSource(ownerPhone) { validate() }
        validate()
    }



    // Eventos de UI
    private val _showSelectSymptomMessage = MutableLiveData<Boolean>(false)
    val showSelectSymptomMessage: LiveData<Boolean> = _showSelectSymptomMessage

    private val _navigateToHome = MutableLiveData<Boolean>(false)
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    // LiveData para indicar si se está cargando la imagen (opcional, para mostrar un ProgressBar)
    private val _isFetchingImage = MutableLiveData<Boolean>(false)
    val isFetchingImage: LiveData<Boolean> get() = _isFetchingImage

    init {
        val appointmentDao = AppDatabase.getDatabase(application).appointmentDao()
        repository = AppointmentRepository(appointmentDao) // Inicializar el repositorio
        loadBreedsFromApi()
    }

    private fun loadBreedsFromApi() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.dogCeoApiService.getAllBreeds()
                if (response.isSuccessful) {
                    val body = response.body()
                    val breeds = body?.message?.keys
                        ?.map { it.replaceFirstChar(Char::titlecase) } // Capitalizar la primera letra
                        ?.sorted()
                        ?: emptyList()
                    _breedList.postValue(breeds)
                } else {
                    _breedList.postValue(emptyList())
                }
            } catch (e: Exception) {
                _breedList.postValue(emptyList())
                // Aquí podrías mostrar un error al usuario si la carga de razas falla
            }
        }
    }

    fun saveAppointment(defaultSymptomText: String) {
        val symptomValue = selectedSymptom.value.orEmpty()
        if (symptomValue.isBlank() || symptomValue == defaultSymptomText) {
            _showSelectSymptomMessage.value = true
            return
        }

        val currentPetName = petName.value.orEmpty()
        val currentBreed = breed.value.orEmpty() // La raza seleccionada en el AutoComplete
        val currentOwnerName = ownerName.value.orEmpty()
        val currentOwnerPhone = ownerPhone.value.orEmpty()

        // Validar que los campos no estén vacíos (aunque el botón ya lo hace, una doble verificación)
        if (currentPetName.isBlank() || currentBreed.isBlank() || currentOwnerName.isBlank() || currentOwnerPhone.isBlank()) {
            // Podrías mostrar un Toast o un error más específico aquí
            return
        }

        _isFetchingImage.value = true // Iniciar indicador de carga

        viewModelScope.launch {
            var imageUrl: String? = null
            try {
                // La API de Dog CEO espera las razas en minúsculas y, si son compuestas (ej. "German Shepherd"),
                // a veces las quiere como "germanshepherd" o "shepherd/german".
                // Para simplificar, usaremos la raza tal como viene del AutoComplete pero en minúsculas.
                // La API de lista de razas devuelve nombres como "germanshepherd", así que eso debería funcionar.
                // Si la raza seleccionada tiene espacios, la API de imagen podría fallar.
                // Una mejora sería normalizar el nombre de la raza para la API de imagen.
                // Por ahora, convertimos a minúsculas.
                val apiBreedName = currentBreed.lowercase().replace(" ", "") // Intento simple de normalización

                if (apiBreedName.isNotBlank()) {
                    val imageResponse = RetrofitClient.dogCeoApiService.getRandomImageByBreed(apiBreedName)
                    if (imageResponse.isSuccessful && imageResponse.body()?.status == "success") {
                        imageUrl = imageResponse.body()?.imageUrl
                    }
                }
            } catch (e: Exception) {
                // Manejar error de red al obtener la imagen, imageUrl permanecerá null
                // Podrías loguear el error: Log.e("NewAppointmentVM", "Error fetching dog image", e)
            }

            val dbEntity = DbAppointment(
                petName = currentPetName,
                breed = currentBreed, // Guardar la raza como la seleccionó el usuario
                ownerName = currentOwnerName,
                ownerPhone = currentOwnerPhone,
                symptoms = symptomValue,
                petImageUrl = imageUrl // Guardar la URL de la imagen obtenida (o null si falló)
            )

            // Guardar en la base de datos usando el repositorio
            // withContext(Dispatchers.IO) { // El DAO ya usa suspend, así que no es estrictamente necesario aquí
            repository.insert(dbEntity)
            // }

            _isFetchingImage.postValue(false) // Finalizar indicador de carga
            _navigateToHome.postValue(true) // Navegar después de guardar
        }
    }

    fun onShowSelectSymptomMessageComplete() {
        _showSelectSymptomMessage.value = false
    }

    fun onNavigationComplete() {
        _navigateToHome.value = false
        // Limpiar campos (opcional, pero buena práctica)
        petName.value = ""
        breed.value = ""
        ownerName.value = ""
        ownerPhone.value = ""
        selectedSymptom.value = ""
    }
}