package com.example.dogapp.home

// Importaciones necesarias para ViewModel, LiveData y acceso a base de datos
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment
import com.example.dogapp.repository.AppointmentRepository

/**
 * ViewModel de la pantalla principal (Home).
 * Encargado de proporcionar la lista de citas agendadas a la UI y abstraer la lógica de acceso a datos.
 * Hereda de AndroidViewModel para tener acceso al contexto de la aplicación (necesario para Room).
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio que encapsula la lógica de acceso a la base de datos de citas
    private val repository: AppointmentRepository
    // LiveData que expone la lista de todas las citas para ser observada por la UI
    val allAppointments: LiveData<List<Appointment>>

    init {
        // Obtiene una instancia del DAO de citas usando el contexto de la aplicación
        val appointmentDao = AppDatabase.getDatabase(application).appointmentDao()

        // Inicializa el repositorio con el DAO obtenido
        repository = AppointmentRepository(appointmentDao)

        // Obtiene el LiveData de todas las citas desde el repositorio
        // Esto permite que la UI se actualice automáticamente cuando cambian los datos en la base
        allAppointments = repository.allAppointments
    }
}

