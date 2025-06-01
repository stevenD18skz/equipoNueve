package com.example.dogapp.editappointment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment
import com.example.dogapp.repository.AppointmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditAppointmentViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository: AppointmentRepository
    private val _originalAppointment = MutableLiveData<Appointment?>()

    val petName = MutableLiveData<String>()
    val breed = MutableLiveData<String>() // Este es el valor actual del campo de raza
    val ownerName = MutableLiveData<String>()
    val ownerPhone = MutableLiveData<String>()

    private val _breedList = MutableLiveData<List<String>>() // Lista para el AutoComplete
    val breedList: LiveData<List<String>> get() = _breedList

    val isEditButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun validate() {
            val petNameValid = !petName.value.isNullOrBlank()
            val breedValid = !breed.value.isNullOrBlank() // Validar el campo de raza actual
            val ownerNameValid = !ownerName.value.isNullOrBlank()
            val ownerPhoneValid = !ownerPhone.value.isNullOrBlank()
            value = petNameValid && breedValid && ownerNameValid && ownerPhoneValid
        }
        addSource(petName) { validate() }
        addSource(breed) { validate() }
        addSource(ownerName) { validate() }
        addSource(ownerPhone) { validate() }
    }

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _navigateToDetail = MutableLiveData<Int?>()
    val navigateToDetail: LiveData<Int?> get() = _navigateToDetail

    // LiveData para indicar si se está cargando la imagen
    private val _isFetchingImage = MutableLiveData<Boolean>(false)
    val isFetchingImage: LiveData<Boolean> get() = _isFetchingImage

    private val appointmentId: Int

    init {
        val appointmentDao = AppDatabase.getDatabase(application).appointmentDao()
        repository = AppointmentRepository(appointmentDao)
        appointmentId = savedStateHandle.get<Int>("appointmentId") ?: -1

        if (appointmentId != -1) {
            loadAppointmentToEdit(appointmentId)
        } else {
            _originalAppointment.value = null
        }
        loadBreedsFromApi() // Cargar lista de razas para el AutoComplete
    }

    private fun loadBreedsFromApi() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.dogCeoApiService.getAllBreeds()
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = body?.message
                        ?.keys
                        ?.map { it.replaceFirstChar(Char::titlecase) }
                        ?.sorted() ?: emptyList()
                    _breedList.postValue(list)
                } else {
                    _breedList.postValue(emptyList())
                }
            } catch (e: Exception) {
                _breedList.postValue(emptyList())
            }
        }
    }

    private fun loadAppointmentToEdit(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val appointmentFromDb = repository.getAppointmentById(id)
            _originalAppointment.postValue(appointmentFromDb)
            appointmentFromDb?.let {
                petName.postValue(it.petName)
                breed.postValue(it.breed) // Pre-rellenar el campo de raza
                ownerName.postValue(it.ownerName)
                ownerPhone.postValue(it.ownerPhone)
            }
        }
    }

    fun updateAppointment() {
        val original = _originalAppointment.value
        val currentBreedValue = breed.value // La raza actual en el campo de texto
        if (original != null && isEditButtonEnabled.value == true && currentBreedValue != null) {
            _isFetchingImage.value = true // Indicar que estamos procesando

            viewModelScope.launch(Dispatchers.IO) {
                var newImageUrl = original.petImageUrl // Por defecto, mantener la imagen original

                // Comprobar si la raza ha cambiado
                if (currentBreedValue.lowercase().trim() != original.breed.lowercase().trim()) {
                    // La raza cambió, intentar obtener una nueva imagen
                    try {
                        val apiBreedName = currentBreedValue.lowercase().replace(" ", "")
                        if (apiBreedName.isNotBlank()) {
                            val imageResponse = RetrofitClient.dogCeoApiService.getRandomImageByBreed(apiBreedName)
                            if (imageResponse.isSuccessful && imageResponse.body()?.status == "success") {
                                newImageUrl = imageResponse.body()?.imageUrl
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                val updatedAppointment = original.copy(
                    petName = petName.value ?: original.petName,
                    breed = currentBreedValue, // Usar la raza actual del campo
                    ownerName = ownerName.value ?: original.ownerName,
                    ownerPhone = ownerPhone.value ?: original.ownerPhone,
                    petImageUrl = newImageUrl // Usar la nueva URL de imagen
                )
                repository.update(updatedAppointment)
                _isFetchingImage.postValue(false) // Finalizar indicador de carga
                _navigateToHome.postValue(true)
            }
        }
    }

    fun onToolbarBackClicked() {
        _navigateToDetail.value = appointmentId
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}

