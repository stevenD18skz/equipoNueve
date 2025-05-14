package com.example.dogapp.repository // Ajusta tu package name

import androidx.lifecycle.LiveData
import com.example.dogapp.database.dao.AppointmentDao
import com.example.dogapp.database.entity.Appointment

class AppointmentRepository(private val appointmentDao: AppointmentDao) {

    val allAppointments: LiveData<List<Appointment>> = appointmentDao.getAllAppointments()

    suspend fun insert(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment)
    }

    suspend fun update(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment)
    }

    suspend fun deleteById(appointmentId: Int) {
        appointmentDao.deleteAppointmentById(appointmentId)
    }

    suspend fun getAppointmentById(appointmentId: Int): Appointment? {
        return appointmentDao.getAppointmentById(appointmentId)
    }
}