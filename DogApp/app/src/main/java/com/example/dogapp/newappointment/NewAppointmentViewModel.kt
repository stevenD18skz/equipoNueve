package com.example.dogapp.newappointment

import androidx.lifecycle.*
import com.example.dogapp.model.Appointment
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch

class NewAppointmentViewModel : ViewModel() {

    val petName = MutableLiveData<String>("")
    val breed = MutableLiveData<String>("")
    val ownerName = MutableLiveData<String>("")
    val ownerPhone = MutableLiveData<String>("")
    val selectedSymptom = MutableLiveData<String>("")

    private val _breedList = MutableLiveData<List<String>>(listOf(
        "Beagle", "Dálmata", "Labrador", "Husky"
    ))
    val breedList: LiveData<List<String>> = _breedList

    val isSaveButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun validate() {
            value = !petName.value.isNullOrBlank()
                    && !breed.value.isNullOrBlank()
                    && !ownerName.value.isNullOrBlank()
                    && !ownerPhone.value.isNullOrBlank()
        }
        addSource(petName) { validate() }
        addSource(breed) { validate() }
        addSource(ownerName) { validate() }
        addSource(ownerPhone) { validate() }
    }

    private val _showSelectSymptomMessage = MutableLiveData<Boolean>()
    val showSelectSymptomMessage: LiveData<Boolean> = _showSelectSymptomMessage

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    fun saveAppointment(defaultSymptomText: String) {
        if (selectedSymptom.value.isNullOrBlank() || selectedSymptom.value == defaultSymptomText) {
            _showSelectSymptomMessage.value = true
            return
        }

        // Simulación de guardado
        val newAppointment = Appointment(
            id = 0,
            petName = petName.value.orEmpty(),
            breed = breed.value.orEmpty(),
            ownerName = ownerName.value.orEmpty(),
            ownerPhone = ownerPhone.value.orEmpty(),
            symptoms = selectedSymptom.value.orEmpty(),
            petImageUrl = null
        )
        println("Guardando cita: $newAppointment")

        _navigateToHome.value = true
    }

    fun onShowSelectSymptomMessageComplete() {
        _showSelectSymptomMessage.value = false
    }

    fun onNavigationComplete() {
        _navigateToHome.value = false
    }
}
