package com.example.dogapp.repository

// Importaciones necesarias para LiveData y acceso a base de datos
import androidx.lifecycle.LiveData
import com.example.dogapp.database.dao.AppointmentDao
import com.example.dogapp.database.entity.Appointment

/**
 * Repositorio que abstrae el acceso a la base de datos de citas.
 * Encapsula la lógica de persistencia y expone métodos para la UI y los ViewModels.
 */
class AppointmentRepository(private val appointmentDao: AppointmentDao) {

    // LiveData con la lista de todas las citas, observado por la UI
    val allAppointments: LiveData<List<Appointment>> = appointmentDao.getAllAppointments()

    /**
     * Inserta una nueva cita en la base de datos (operación suspendida)
     */
    suspend fun insert(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment)
    }

    /**
     * Actualiza una cita existente en la base de datos (operación suspendida)
     */
    suspend fun update(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment)
    }

    /**
     * Elimina una cita por su ID (operación suspendida)
     */
    suspend fun deleteById(appointmentId: Int) {
        appointmentDao.deleteAppointmentById(appointmentId)
    }

    /**
     * Obtiene una cita por su ID (operación suspendida)
     */
    suspend fun getAppointmentById(appointmentId: Int): Appointment? {
        return appointmentDao.getAppointmentById(appointmentId)
    }
}