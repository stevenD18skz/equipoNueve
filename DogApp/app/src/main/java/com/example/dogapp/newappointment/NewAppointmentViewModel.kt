package com.example.dogapp.newappointment // Ajusta tu package name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

import com.example.dogapp.model.Appointment

import androidx.lifecycle.*
import com.example.dogapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch


class NewAppointmentViewModel : ViewModel() {

    // LiveData para los campos del formulario
    val petName = MutableLiveData<String>("")
    val breed = MutableLiveData<String>("")
    val ownerName = MutableLiveData<String>("")
    val ownerPhone = MutableLiveData<String>("")
    val selectedSymptom = MutableLiveData<String>("") // Para rastrear el síntoma seleccionado

    // Mock data para la lista de razas (simula la API)
    private val _breedList = MutableLiveData<List<String>>()
    val breedList: LiveData<List<String>> get() = _breedList

    // LiveData para habilitar/deshabilitar el botón Guardar
    // Criterio 7 y 9: Habilitado si los campos obligatorios no están vacíos
    // LiveData para habilitar/deshabilitar el botón Guardar
    // Criterio 7 y 9: Habilitado si los campos obligatorios no están vacíos
    val isSaveButtonEnabled: LiveData<Boolean> = petName.map { it.orEmpty().isNotBlank() } // it.orEmpty() para asegurar que isNotBlank se llama en un String no nulo
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

    // Evento para mostrar mensaje de "Selecciona un síntoma"
    private val _showSelectSymptomMessage = MutableLiveData<Boolean>()
    val showSelectSymptomMessage: LiveData<Boolean> get() = _showSelectSymptomMessage

    // Evento para navegar de vuelta al Home después de guardar
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    // Reemplaza la lista mock con esta implementación

    init {
        loadBreeds()
    }

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

    // Criterio 10: Simula guardar la cita
    fun saveAppointment(defaultSymptomText: String) {
        // Criterio 8: Validar que se haya seleccionado un síntoma válido
        if (selectedSymptom.value.isNullOrBlank() || selectedSymptom.value == defaultSymptomText) {
            _showSelectSymptomMessage.value = true
            return
        }

        // Aquí iría la lógica para crear el objeto Appointment y guardarlo
        // usando el repositorio (cuando implementes Room).
        // Por ahora, solo simulamos el éxito y activamos la navegación.
        val newAppointment = Appointment(
            id = 0, // El ID real lo generaría Room
            petName = petName.value ?: "",
            breed = breed.value ?: "",
            ownerName = ownerName.value ?: "",
            ownerPhone = ownerPhone.value ?: "",
            symptoms = selectedSymptom.value ?: "",
            petImageUrl = null // La URL de la imagen se obtendría aquí en la implementación real
        )
        println("Simulando guardado de cita: $newAppointment") // Log para depuración

        _navigateToHome.value = true // Activa el evento de navegación
    }

    fun onShowSelectSymptomMessageComplete() {
        _showSelectSymptomMessage.value = false // Resetea el evento
    }

    fun onNavigationComplete() {
        _navigateToHome.value = false // Resetea el evento
    }
}

// Helper function para combinar LiveData (puedes ponerla en un archivo utils/LiveDataExt.kt)
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