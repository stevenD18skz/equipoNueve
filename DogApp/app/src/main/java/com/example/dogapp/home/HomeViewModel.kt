package com.example.dogapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogapp.model.Appointment


class HomeViewModel : ViewModel() {

    // Usamos MutableLiveData para poder asignarle los datos mock
    private val _appointments = MutableLiveData<List<Appointment>>()
    val allAppointments: LiveData<List<Appointment>>
        get() = _appointments // Exponemos como LiveData inmutable

    init {
        // Cargamos los datos mock al inicializar el ViewModel
        loadMockAppointments()
    }

    private fun loadMockAppointments() {
        // Creamos una lista de datos de prueba
        val mockList = listOf(
            Appointment(
                id = 1,
                petName = "Cory",
                breed = "Beagle",
                ownerName = "Juan",
                ownerPhone = "3001112233",
                symptoms = "Fractura extremidad",
                petImageUrl = null
            ),
            Appointment(
                id = 2,
                petName = "Zeus",
                breed = "Dalmata",
                ownerName = "Maria",
                ownerPhone = "3014445566",
                symptoms = "Solo duerme",
                petImageUrl = null
            ),
            Appointment(
                id = 3,
                petName = "Rocky",
                breed = "Labrador",
                ownerName = "Pedro",
                ownerPhone = "3027778899",
                symptoms = "No come",
                petImageUrl = null
            ),
            Appointment(
                id = 4,
                petName = "Luna",
                breed = "Husky",
                ownerName = "Ana",
                ownerPhone = "3109998877",
                symptoms = "Tiene pulgas",
                petImageUrl = null
            )
        )
        _appointments.value = mockList
    }
}