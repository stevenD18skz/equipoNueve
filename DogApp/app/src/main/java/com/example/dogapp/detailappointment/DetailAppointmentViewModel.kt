package com.example.dogapp.detailappointment

import android.app.Application // Necesario para AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel // Cambiamos ViewModel por AndroidViewModel
import androidx.lifecycle.viewModelScope // Para coroutines
import com.example.dogapp.model.Appointment
import com.example.dogapp.database.AppDatabase // Asegúrate que la ruta a tu AppDatabase sea correcta
import com.example.dogapp.database.dao.AppointmentDao // Asegúrate que la ruta a tu DAO sea correcta
import kotlinx.coroutines.launch // Para lanzar coroutines

class AppointmentDetailViewModel(
    application: Application // Recibimos Application
    // private val savedStateHandle: SavedStateHandle // Puedes considerar usar esto para el ID
) : AndroidViewModel(application) { // Heredamos de AndroidViewModel

    // Obtenemos una instancia del DAO.
    // Es mejor inyectarlo mediante Hilt o una Factory, pero para seguir el patrón
    // del ejemplo de HomeViewModel, lo obtenemos directamente aquí.
    private val appointmentDao: AppointmentDao = AppDatabase.getDatabase(application).appointmentDao()

    private val _appointment = MutableLiveData<Appointment?>()
    val appointment: LiveData<Appointment?> = _appointment

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    private val _navigateToEdit = MutableLiveData<Int?>()
    val navigateToEdit: LiveData<Int?> = _navigateToEdit

    /**
     * Llama esto en el Fragment para pasar el ID desde Safe Args y cargar los datos.
     */
    fun init(appointmentId: Int) {
        // Evita recargar si ya tenemos la cita (aunque con LiveData de Room esto se maneja diferente)
        // o si el ID no es válido.
        if (appointmentId < 0) {
            _appointment.value = null // O manejar como un error
            return
        }
        loadAppointmentDetail(appointmentId)
    }

    private fun loadAppointmentDetail(id: Int) {
        viewModelScope.launch {
            // Aquí asumimos que tu DAO tiene un método getAppointmentById que devuelve un Appointment?
            // Si devuelve LiveData<Appointment?>, la lógica sería un poco diferente (asignarías directamente o usarías Transformations).
            // Por simplicidad y para operaciones únicas, un suspend fun es común para "obtener una vez".
            val appointmentFromDb = appointmentDao.getAppointmentById(id) // Asume suspend fun
            _appointment.postValue(appointmentFromDb)
        }
    }

    fun onDeleteClicked() {
        _appointment.value?.let { currentAppointment ->
            viewModelScope.launch {
                appointmentDao.delete(currentAppointment) // Asume suspend fun delete(appointment: Appointment)
                _navigateToHome.postValue(true) // Navegar después de borrar
            }
        } ?: run {
            // No hay cita para borrar, quizás mostrar un error o simplemente navegar.
            _navigateToHome.value = true
        }
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun onEditClicked() {
        _navigateToEdit.value = _appointment.value?.id
    }

    fun onEditNavigated() {
        _navigateToEdit.value = null
    }
}