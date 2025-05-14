package com.example.dogapp.edit // Ajusta tu package name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.model.Appointment
import com.example.dogapp.newappointment.combineWith
import kotlinx.coroutines.launch

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


class EditAppointmentViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _originalAppointment = MutableLiveData<Appointment?>()
    // val originalAppointment: LiveData<Appointment?> get() = _originalAppointment // Si se necesita externamente

    // LiveData para los campos editables del formulario
    val petName = MutableLiveData<String>()
    val breed = MutableLiveData<String>()
    val ownerName = MutableLiveData<String>()
    val ownerPhone = MutableLiveData<String>()
    // Síntomas no son editables en esta HU, pero podríamos necesitarlos si el objeto Appointment se reutiliza
    // val symptoms = MutableLiveData<String>()

    // Mock data para la lista de razas
    private val _breedList = MutableLiveData<List<String>>()
    val breedList: LiveData<List<String>> get() = _breedList

    // LiveData para habilitar/deshabilitar el botón Guardar
    val isEditButtonEnabled: LiveData<Boolean> = petName.map { it.orEmpty().isNotBlank() } // it.orEmpty() para asegurar que isNotBlank se llama en un String no nulo
        .combineWith(breed) { nameValid, breedText ->
            // Si nameValid es null, lo tratamos como false.
            // Si breedText es null, isNotBlank() devolverá false.
            (nameValid ?: false) && (breedText.orEmpty().isNotBlank())
        }
        .combineWith(ownerName) { prevValid, ownerText ->
            // Lo mismo para las siguientes combinaciones
            (prevValid ?: false) && (ownerText.orEmpty().isNotBlank())
        }
        .combineWith(ownerPhone) { prevValid, phoneText ->
            (prevValid ?: false) && (phoneText.orEmpty().isNotBlank())
        }

    // Evento para navegar al Home después de editar
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    // Evento para navegar de vuelta al Detalle (botón atrás de la toolbar)
    private val _navigateToDetail = MutableLiveData<Int?>() // Contendrá el ID de la cita original
    val navigateToDetail: LiveData<Int?> get() = _navigateToDetail


    init {
        val appointmentId = savedStateHandle.get<Int>("appointmentId") ?: -1
        if (appointmentId != -1) {
            loadMockAppointmentToEdit(appointmentId)
            loadBreeds()
        } else {
            // Manejar caso de ID inválido, quizás navegar atrás o mostrar error
            _originalAppointment.value = null
        }
    }

    private fun loadMockAppointmentToEdit(id: Int) {
        // Simula la carga de una cita específica desde una lista mock
        val mockAppointments = listOf(
            Appointment(1, "Cory", "Beagle", "Juan Pérez", "3001112233", "Fractura extremidad", null),
            Appointment(2, "Zeus", "Dalmata", "Maria López", "3014445566", "Solo duerme", null),
            Appointment(3, "Rocky", "Labrador", "Pedro Gómez", "3027778899", "No come", null),
            Appointment(4, "Luna", "Husky", "Ana Torres", "3109998877", "Tiene pulgas", null)
            // Asegúrate que estos IDs coincidan con los que navegas desde Home/Detail
        )
        val appointmentToEdit = mockAppointments.find { it.id == id }
        _originalAppointment.value = appointmentToEdit

        // Pre-rellenar los LiveData de los campos
        appointmentToEdit?.let {
            petName.value = it.petName
            breed.value = it.breed
            ownerName.value = it.ownerName
            ownerPhone.value = it.ownerPhone
            // symptoms.value = it.symptoms // Si fuera editable
        }
    }

    // Reemplaza la lista mock con esta implementación
    private fun loadBreeds() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.dogCeoApiService.getAllBreeds()
                if (response.isSuccessful) {
                    val breeds = response.body()?.message?.keys?.toList() ?: emptyList()
                    _breedList.postValue(breeds.sorted()) // Orden alfabético
                } else {
                    _breedList.postValue(emptyList())
                }
            } catch (e: Exception) {
                _breedList.postValue(emptyList())
            }
        }
    }

    fun updateAppointment() {
        // Criterio 8: Simula la modificación y navega a Home
        // En una app real, construirías el objeto Appointment actualizado y lo pasarías al repositorio.
        val updatedAppointment = Appointment(
            id = _originalAppointment.value?.id ?: 0, // Mantener el ID original
            petName = petName.value ?: "",
            breed = breed.value ?: "",
            ownerName = ownerName.value ?: "",
            ownerPhone = ownerPhone.value ?: "",
            symptoms = _originalAppointment.value?.symptoms ?: "", // Mantener síntomas originales
            petImageUrl = _originalAppointment.value?.petImageUrl // Mantener imagen original
        )
        println("Simulando actualización de cita: $updatedAppointment") // Log para depuración

        _navigateToHome.value = true // Activa el evento de navegación a Home
    }

    fun onToolbarBackClicked() {
        _navigateToDetail.value = _originalAppointment.value?.id
    }


    fun onNavigationToHomeComplete() {
        _navigateToHome.value = false
    }

    fun onNavigationToDetailComplete() {
        _navigateToDetail.value = null
    }
}