package com.example.dogapp.newappointment

import android.app.Application
import androidx.lifecycle.*
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment as DbAppointment
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.model.Appointment
import kotlinx.coroutines.launch

class NewAppointmentViewModel(
    application: Application
) : AndroidViewModel(application) {

    // DAO de la base de datos Room
    private val appointmentDao = AppDatabase
        .getDatabase(application)
        .appointmentDao()

    // Campos del formulario
    val petName        = MutableLiveData<String>("")
    val breed          = MutableLiveData<String>("")
    val ownerName      = MutableLiveData<String>("")
    val ownerPhone     = MutableLiveData<String>("")
    val selectedSymptom= MutableLiveData<String>("")

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

    init {
        loadBreedsFromApi()
    }

    /** Carga la lista de razas desde Dog CEO y la publica en _breedList */
    private fun loadBreedsFromApi() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.dogCeoApiService.getAllBreeds()
                if (response.isSuccessful) {
                    // La estructura JSON es { message: { breedName: [subBreeds] }, status: "success" }
                    val body = response.body()
                    val breeds = body?.message?.keys
                        ?.map { it.replaceFirstChar(Char::titlecase) }
                        ?.sorted()
                        ?: emptyList()
                    _breedList.postValue(breeds)
                } else {
                    // Si falla el HTTP, dejamos la lista vacía
                    _breedList.postValue(emptyList())
                }
            } catch (e: Exception) {
                // En caso de excepción de red, también vaciamos la lista
                _breedList.postValue(emptyList())
            }
        }
    }

    /**
     * Invocado al pulsar Guardar.
     * Valida que se haya elegido síntoma y, si todo OK, guarda en Room y navega.
     */
    fun saveAppointment(defaultSymptomText: String) {
        val symptom = selectedSymptom.value.orEmpty()
        if (symptom.isBlank() || symptom == defaultSymptomText) {
            _showSelectSymptomMessage.value = true
            return
        }

        // Crear la entidad de Room
        val dbEntity = DbAppointment(
            petName    = petName.value.orEmpty(),
            breed      = breed.value.orEmpty(),
            ownerName  = ownerName.value.orEmpty(),
            ownerPhone = ownerPhone.value.orEmpty(),
            symptoms   = symptom,
            petImageUrl= null
        )

        viewModelScope.launch {
            appointmentDao.insertAppointment(dbEntity)
            _navigateToHome.postValue(true)
        }
    }

    fun onShowSelectSymptomMessageComplete() {
        _showSelectSymptomMessage.value = false
    }

    fun onNavigationComplete() {
        _navigateToHome.value = false
    }
}
