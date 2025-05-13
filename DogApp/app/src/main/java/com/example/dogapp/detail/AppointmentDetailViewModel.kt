package com.example.dogapp.detail // Ajusta tu package name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle // Para recibir el ID del appointment
import com.example.dogapp.model.Appointment

class AppointmentDetailViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _appointment = MutableLiveData<Appointment?>()
    val appointment: LiveData<Appointment?> get() = _appointment

    // Eventos de navegación
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _navigateToEdit = MutableLiveData<Int?>() // Contendrá el ID para editar
    val navigateToEdit: LiveData<Int?> get() = _navigateToEdit


    init {
        // Recupera el ID de la cita pasado a través de SavedStateHandle (por Safe Args o Bundle)
        val appointmentId = savedStateHandle.get<Int>("appointmentId") ?: -1
        if (appointmentId != -1) {
            loadMockAppointmentDetail(appointmentId)
        } else {
            _appointment.value = null // o manejar error
        }
    }

    private fun loadMockAppointmentDetail(id: Int) {
        // Simula la carga de un detalle de cita específico desde una lista mock
        // En una app real, esto vendría de un repositorio/base de datos
        val mockAppointments = listOf(
            Appointment(1, "Cory", "Beagle", "Juan Pérez", "3001112233", "Fractura extremidad", null),
            Appointment(2, "Zeus", "Dalmata", "Maria López", "3014445566", "Solo duerme", null),
            Appointment(3, "Rocky", "Labrador", "Pedro Gómez", "3027778899", "No come", null),
            Appointment(4, "Luna", "Husky", "Ana Torres", "3109998877", "Tiene pulgas", null)
            // Añade más si los tienes en tu HomeViewModel mock, deben coincidir los IDs
        )
        _appointment.value = mockAppointments.find { it.id == id }
    }

    fun onDeleteClicked() {
        // En un escenario real, aquí llamarías al repositorio para eliminar.
        // Con mocks, simplemente activamos la navegación.
        // Podrías simular la eliminación de la lista mock si la compartieras
        // o a través de un evento a HomeViewModel, pero para UI es más simple navegar.
        _navigateToHome.value = true
    }

    fun onNavigateToHomeComplete() {
        _navigateToHome.value = false // Resetear el evento
    }

    fun onEditClicked() {
        _navigateToEdit.value = appointment.value?.id
    }

    fun onNavigateToEditComplete() {
        _navigateToEdit.value = null // Resetear el evento
    }
}