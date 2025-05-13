package com.example.dogapp.home // Ajusta tu package name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogapp.model.Appointment // Importa la clase de datos simple

// Cambiamos a ViewModel ya que no necesitamos el Context para la DB
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
                petImageUrl = null // Puedes poner una URL de imagen si quieres probar Coil, ej: "https://..."
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
        // Asignamos la lista al MutableLiveData
        _appointments.value = mockList
    }

    // Si necesitas simular la carga/eliminación/actualización,
    // puedes añadir funciones aquí que modifiquen la lista en _appointments.value
    // Ejemplo (no requerido por ahora):
    /*
    fun removeAppointment(appointmentId: Int) {
        val currentList = _appointments.value?.toMutableList()
        currentList?.removeAll { it.id == appointmentId }
        _appointments.value = currentList
    }
    */
}