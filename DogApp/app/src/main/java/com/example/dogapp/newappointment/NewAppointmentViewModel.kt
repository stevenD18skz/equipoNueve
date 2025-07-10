package com.example.dogapp.newappointment

// Importaciones necesarias para ViewModel, LiveData, corrutinas y acceso a base de datos
import android.app.Application
import androidx.lifecycle.*
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment as DbAppointment // Alias para evitar colisión
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.repository.AppointmentRepository // Necesitarás el repositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Función de extensión para combinar dos LiveData y producir un nuevo LiveData
 * útil para validaciones o dependencias entre campos del formulario.
 */
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

/**
 * ViewModel para el registro de nuevas citas veterinarias.
 * Gestiona la lógica de validación, acceso a datos y comunicación con la UI.
 */
class NewAppointmentViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Repositorio para interactuar con la base de datos
    private val repository: AppointmentRepository

    // Campos del formulario: cada uno es un LiveData editable
    /**
     * Nombre de la mascota.
     * Valor inicial: cadena vacía.
     */
    val petName = MutableLiveData<String>("")

    /**
     * Raza seleccionada.
     * Valor inicial: cadena vacía.
     */
    val breed = MutableLiveData<String>("")

    /**
     * Nombre del dueño.
     * Valor inicial: cadena vacía.
     */
    val ownerName = MutableLiveData<String>("")

    /**
     * Teléfono del dueño.
     * Valor inicial: cadena vacía.
     */
    val ownerPhone = MutableLiveData<String>("")

    /**
     * Síntoma seleccionado.
     * Valor inicial: cadena vacía.
     */
    val selectedSymptom = MutableLiveData<String>("")

    // Lista de razas obtenida desde la API
    /**
     * Lista de razas disponibles.
     * Valor inicial: lista vacía.
     */
    private val _breedList = MutableLiveData<List<String>>(emptyList())
    val breedList: LiveData<List<String>> = _breedList

    // Botón habilitado si los campos obligatorios no están vacíos
    /**
     * Indica si el botón de guardar está habilitado.
     * Se habilita si todos los campos obligatorios tienen un valor no vacío.
     */
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
    /**
     * Indica si se debe mostrar un mensaje para seleccionar un síntoma.
     * Valor inicial: falso.
     */
    private val _showSelectSymptomMessage = MutableLiveData<Boolean>(false)
    val showSelectSymptomMessage: LiveData<Boolean> = _showSelectSymptomMessage

    private val _navigateToHome = MutableLiveData<Boolean>(false)
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    // LiveData para indicar si se está cargando la imagen
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
        val currentBreed = breed.value.orEmpty()
        val currentOwnerName = ownerName.value.orEmpty()
        val currentOwnerPhone = ownerPhone.value.orEmpty()

        // Validar que los campos no estén vacíos
        if (currentPetName.isBlank() || currentBreed.isBlank() || currentOwnerName.isBlank() || currentOwnerPhone.isBlank()) {
            return
        }

        _isFetchingImage.value = true // Iniciar indicador de carga

        viewModelScope.launch {
            var imageUrl: String? = null
            try {
                val apiBreedName = currentBreed.lowercase().replace(" ", "")

                if (apiBreedName.isNotBlank()) {
                    val imageResponse = RetrofitClient.dogCeoApiService.getRandomImageByBreed(apiBreedName)
                    if (imageResponse.isSuccessful && imageResponse.body()?.status == "success") {
                        imageUrl = imageResponse.body()?.imageUrl
                    }
                }
            } catch (e: Exception) {
            }

            val dbEntity = DbAppointment(
                petName = currentPetName,
                breed = currentBreed,
                ownerName = currentOwnerName,
                ownerPhone = currentOwnerPhone,
                symptoms = symptomValue,
                petImageUrl = imageUrl
            )

            repository.insert(dbEntity)
            _isFetchingImage.postValue(false) // Finalizar indicador de carga
            _navigateToHome.postValue(true) // Navegar después de guardar
        }
    }

    fun onShowSelectSymptomMessageComplete() {
        _showSelectSymptomMessage.value = false
    }

    fun onNavigationComplete() {
        _navigateToHome.value = false
        petName.value = ""
        breed.value = ""
        ownerName.value = ""
        ownerPhone.value = ""
        selectedSymptom.value = ""
    }
}