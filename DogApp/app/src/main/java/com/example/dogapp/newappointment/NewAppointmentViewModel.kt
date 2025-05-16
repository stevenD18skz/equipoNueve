package com.example.dogapp.newappointment

import android.app.Application // Necesario para AndroidViewModel
import androidx.lifecycle.*
import com.example.dogapp.model.Appointment
import com.example.dogapp.database.AppDatabase // Asegúrate que la ruta sea correcta
import com.example.dogapp.database.dao.AppointmentDao // Asegúrate que la ruta sea correcta
import kotlinx.coroutines.launch // Para lanzar coroutines

class NewAppointmentViewModel(
    application: Application // Recibimos Application
) : AndroidViewModel(application) { // Heredamos de AndroidViewModel

    // Obtenemos una instancia del DAO
    private val appointmentDao: AppointmentDao = AppDatabase.getDatabase(application).appointmentDao()

    val petName = MutableLiveData<String>("")
    val breed = MutableLiveData<String>("")
    val ownerName = MutableLiveData<String>("")
    val ownerPhone = MutableLiveData<String>("")
    val selectedSymptom = MutableLiveData<String>("") // Este es el síntoma que el usuario escribe o selecciona.

    // Esta lista de razas podría venir de la base de datos en el futuro o de recursos de strings.
    private val _breedList = MutableLiveData<List<String>>(listOf(
        "Beagle", "Dálmata", "Labrador", "Husky", "Otro" // Añadí "Otro" por si acaso
    ))
    val breedList: LiveData<List<String>> = _breedList

    val isSaveButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun validate() {
            value = !petName.value.isNullOrBlank() &&
                    !breed.value.isNullOrBlank() &&
                    !ownerName.value.isNullOrBlank() &&
                    !ownerPhone.value.isNullOrBlank()
            // No incluimos selectedSymptom aquí porque se valida en saveAppointment
        }
        addSource(petName) { validate() }
        addSource(breed) { validate() }
        addSource(ownerName) { validate() }
        addSource(ownerPhone) { validate() }
        // Inicializa la validación
        validate()
    }

    private val _showSelectSymptomMessage = MutableLiveData<Boolean>()
    val showSelectSymptomMessage: LiveData<Boolean> = _showSelectSymptomMessage

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    fun saveAppointment(defaultSymptomText: String) {
        val currentSymptom = selectedSymptom.value.orEmpty()
        if (currentSymptom.isBlank() || currentSymptom == defaultSymptomText) {
            _showSelectSymptomMessage.value = true
            return
        }

        // Creamos el objeto Appointment.
        // El ID se genera automáticamente por Room si es 0 y la entidad está configurada con autoGenerate = true.
        // Si no, Room lo tratará como el ID a insertar. Para nuevas citas, usualmente el ID es 0 o null
        // para que Room lo autogenere. Verifica la definición de tu entidad Appointment.
        // Asumiré que tu entidad Appointment tiene id como Int y puede ser autogenerado.
        val newAppointment = com.example.dogapp.database.entity.Appointment( // Usamos la entidad de la BD
            // id = 0, // Si tu ID es autogenerado (autoGenerate = true) y es Long o Int, puedes omitirlo o poner 0
            petName = petName.value.orEmpty(),
            breed = breed.value.orEmpty(),
            ownerName = ownerName.value.orEmpty(),
            ownerPhone = ownerPhone.value.orEmpty(),
            symptoms = currentSymptom,
            petImageUrl = null // Puedes asignar una URL por defecto o dejarla para edición posterior
        )

        viewModelScope.launch {
            appointmentDao.insertAppointment(newAppointment) // Usamos el método del DAO
            _navigateToHome.postValue(true) // Navegar después de guardar
        }
    }

    fun onShowSelectSymptomMessageComplete() {
        _showSelectSymptomMessage.value = false
    }

    fun onNavigationComplete() {
        _navigateToHome.value = false
    }
}