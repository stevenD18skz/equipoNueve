package com.example.dogapp.home // Asegúrate que el package name sea el correcto

import android.app.Application // Necesario para AndroidViewModel
import androidx.lifecycle.AndroidViewModel // Cambiamos de ViewModel a AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.entity.Appointment // IMPORTANTE: Usar la entidad de Room
import com.example.dogapp.repository.AppointmentRepository

// 1. Heredar de AndroidViewModel para obtener el Application context
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // 2. Declarar el Repositorio y el LiveData para las citas
    private val repository: AppointmentRepository
    val allAppointments: LiveData<List<Appointment>> // Este LiveData ahora contendrá List<database.entity.Appointment>

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

        // Ya no necesitamos loadMockAppointments() aquí, porque los datos vendrán de la base de datos.
    }

    // Si necesitas alguna función adicional en el ViewModel para el Home, iría aquí.
    // Por ejemplo, si tuvieras un filtro o una acción específica para el Home.
    // Por ahora, solo exponer 'allAppointments' es suficiente para la HU 2.0.
}

