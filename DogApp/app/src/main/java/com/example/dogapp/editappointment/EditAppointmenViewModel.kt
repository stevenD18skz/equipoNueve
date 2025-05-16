package com.example.dogapp.editappointment // <<<--- PAQUETE CORREGIDO

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
    val breed = MutableLiveData<String>()
    val ownerName = MutableLiveData<String>()
    val ownerPhone = MutableLiveData<String>()

    private val _breedList = MutableLiveData<List<String>>()
    val breedList: LiveData<List<String>> get() = _breedList

    val isEditButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun validate() {
            val petNameValid = !petName.value.isNullOrBlank()
            val breedValid = !breed.value.isNullOrBlank()
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
        loadMockBreeds()
    }

    private fun loadMockBreeds() {
        _breedList.value = listOf(
            "Beagle", "Dálmata", "Labrador", "Husky", "Akita", "Poodle", "Pug", "Boxer",
            "Chihuahua", "Pastor Alemán", "Golden Retriever", "Shih Tzu", "Bulldog Francés"
        )
    }

    private fun loadAppointmentToEdit(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val appointmentFromDb = repository.getAppointmentById(id)
            _originalAppointment.postValue(appointmentFromDb)
            appointmentFromDb?.let {
                petName.postValue(it.petName)
                breed.postValue(it.breed)
                ownerName.postValue(it.ownerName)
                ownerPhone.postValue(it.ownerPhone)
            }
        }
    }

    fun updateAppointment() {
        val original = _originalAppointment.value
        if (original != null && isEditButtonEnabled.value == true) {
            val updatedAppointment = original.copy(
                petName = petName.value ?: original.petName,
                breed = breed.value ?: original.breed,
                ownerName = ownerName.value ?: original.ownerName,
                ownerPhone = ownerPhone.value ?: original.ownerPhone
            )
            viewModelScope.launch(Dispatchers.IO) {
                repository.update(updatedAppointment)
                _navigateToHome.postValue(true)
            }
        }
    }

    fun onToolbarBackClicked() { // Cambiado de onBackPressed para coincidir con el listener del fragment
        _navigateToDetail.value = appointmentId
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}
