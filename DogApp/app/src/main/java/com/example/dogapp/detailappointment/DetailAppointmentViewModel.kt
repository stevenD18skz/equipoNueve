// src/main/java/com/example/dogapp/detailappointment/AppointmentDetailViewModel.kt
package com.example.dogapp.detailappointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dogapp.model.Appointment

class AppointmentDetailViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _appointment = MutableLiveData<Appointment?>()
    val appointment: LiveData<Appointment?> = _appointment

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    private val _navigateToEdit = MutableLiveData<Int?>()
    val navigateToEdit: LiveData<Int?> = _navigateToEdit

    /** Llama esto en el Fragment para pasar el ID desde Safe Args */
    fun init(appointmentId: Int) {
        if (_appointment.value != null) return  // ya inicializado
        if (appointmentId >= 0) {
            loadMockAppointmentDetail(appointmentId)
        } else {
            _appointment.value = null
        }
    }

    private fun loadMockAppointmentDetail(id: Int) {
        val mockAppointments = listOf(
            Appointment(1, "Cory", "Beagle", "Juan Pérez", "3001112233", "Fractura extremidad", null),
            Appointment(2, "Zeus", "Dálmata", "María López", "3014445566", "Solo duerme", null),
            Appointment(3, "Rocky", "Labrador", "Pedro Gómez", "3027778899", "No come", null),
            Appointment(4, "Luna", "Husky", "Ana Torres", "3109998877", "Tiene pulgas", null)
        )
        _appointment.value = mockAppointments.find { it.id == id }
    }

    fun onDeleteClicked() {
        // Aquí iría la lógica real de borrado...
        _navigateToHome.value = true
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
