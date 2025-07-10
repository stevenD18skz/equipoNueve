package com.example.dogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dogapp.database.entity.Appointment

/**
 * Data Access Object (DAO) para la entidad Appointment.
 * Define las operaciones de base de datos (CRUD) usando anotaciones de Room.
 */
@Dao
interface AppointmentDao {

    /**
     * Inserta una cita en la base de datos.
     * Si hay conflicto (misma clave primaria), reemplaza el registro.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    /**
     * Actualiza una cita existente en la base de datos.
     */
    @Update
    suspend fun updateAppointment(appointment: Appointment)

    /**
     * Elimina una cita específica de la base de datos.
     */
    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    /**
     * Elimina una cita por su ID (clave primaria).
     * @param appointmentId ID de la cita a eliminar.
     */
    @Query("DELETE FROM appointments WHERE id = :appointmentId")
    suspend fun deleteAppointmentById(appointmentId: Int)

    /**
     * Obtiene todas las citas ordenadas por ID ascendente.
     * El resultado es LiveData, por lo que la UI se actualiza automáticamente.
     */
    @Query("SELECT * FROM appointments ORDER BY id ASC")
    fun getAllAppointments(): LiveData<List<Appointment>>

    /**
     * Obtiene una cita específica por su ID.
     * @param appointmentId ID de la cita a buscar.
     * @return La cita encontrada o null si no existe.
     */
    @Query("SELECT * FROM appointments WHERE id = :appointmentId")
    suspend fun getAppointmentById(appointmentId: Int): Appointment?
}