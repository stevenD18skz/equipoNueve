// src/main/java/com/example/dogapp/editappointment/EditAppointmentViewModel.kt
package com.example.dogapp.editappointment

import androidx.lifecycle.*
import com.example.dogapp.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditAppointmentViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Original para saber ID y datos no editables
    private val _original = MutableLiveData<Appointment?>()

    // Campos editables
    val petName = MutableLiveData<String>()
    val breed = MutableLiveData<String>()
    val ownerName = MutableLiveData<String>()
    val ownerPhone = MutableLiveData<String>()

    // Lista de razas (mock o remoto)
    private val _breedList = MutableLiveData<List<String>>(listOf(
        "Beagle", "Dálmata", "Labrador", "Husky" // O cargar de API real
    ))
    val breedList: LiveData<List<String>> = _breedList

    // Validación de formulario
    val isEditButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun validate() {
            val ok = !petName.value.isNullOrBlank()
                    && !breed.value.isNullOrBlank()
                    && !ownerName.value.isNullOrBlank()
                    && !ownerPhone.value.isNullOrBlank()
            value = ok
        }
        addSource(petName) { validate() }
        addSource(breed) { validate() }
        addSource(ownerName) { validate() }
        addSource(ownerPhone) { validate() }
    }

    // Eventos de navegación
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    private val _navigateToDetail = MutableLiveData<Int?>()
    val navigateToDetail: LiveData<Int?> = _navigateToDetail

    /** Llamar desde Fragment en onViewCreated */
    fun init(appointmentId: Int) {
        if (_original.value != null) return
        if (appointmentId >= 0) loadAppointment(appointmentId)
        else _original.value = null
    }

    private fun loadAppointment(id: Int) {
        // Aquí simulas cargar o llamas a repositorio real
        viewModelScope.launch(Dispatchers.IO) {
            val mock = listOf(
                Appointment(1, "Cory", "Beagle", "Juan Pérez", "3001112233", "Fractura", null),
                Appointment(2, "Zeus", "Dálmata", "María López", "3014445566", "Solo duerme", null),
                Appointment(3, "Rocky", "Labrador", "Pedro Gómez", "3027778899", "No come", null),
                Appointment(4, "Luna", "Husky", "Ana Torres", "3109998877", "Tiene pulgas", null)
            ).find { it.id == id }
            _original.postValue(mock)
            mock?.let {
                petName.postValue(it.petName)
                breed.postValue(it.breed)
                ownerName.postValue(it.ownerName)
                ownerPhone.postValue(it.ownerPhone)
            }
        }
    }

    fun updateAppointment() {
        // Aquí harías el PUT/UPDATE real...
        val updated = _original.value?.copy(
            petName = petName.value.orEmpty(),
            breed = breed.value.orEmpty(),
            ownerName = ownerName.value.orEmpty(),
            ownerPhone = ownerPhone.value.orEmpty()
        )
        // Log.debug(updated)…
        _navigateToHome.value = true
    }

    fun onBackPressed() {
        _navigateToDetail.value = _original.value?.id
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}
