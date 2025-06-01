package com.example.dogapp.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment
import com.example.dogapp.repository.AppointmentRepository

// 1. Heredar de AndroidViewModel para obtener el Application context
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // 2. Declarar el Repositorio y el LiveData para las citas
    private val repository: AppointmentRepository
    val allAppointments: LiveData<List<Appointment>>

    init {
        // 3. Obtener una instancia del DAO a través de la base de datos
        // Se usa el 'application' context que provee AndroidViewModel
        val appointmentDao = AppDatabase.getDatabase(application).appointmentDao()

        // 4. Inicializar el Repositorio con el DAO
        repository = AppointmentRepository(appointmentDao)

        // 5. Obtener todas las citas del Repositorio.
        // El Repositorio obtiene esto del DAO, que a su vez lo obtiene de la base de datos SQLite.
        // Como el DAO devuelve LiveData, la UI se actualizará automáticamente cuando los datos cambien.
        allAppointments = repository.allAppointments
    }
}

