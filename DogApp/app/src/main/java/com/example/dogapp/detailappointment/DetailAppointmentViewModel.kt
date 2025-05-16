package com.example.dogapp.detailappointment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// Importa AMBOS tipos si son diferentes y necesitas el modelo para el LiveData
import com.example.dogapp.model.Appointment as ModelAppointment // Alias para el modelo
import com.example.dogapp.database.entity.Appointment as EntityAppointment // Alias para la entidad (opcional si las rutas son claras)
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.dao.AppointmentDao
import kotlinx.coroutines.launch

class AppointmentDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appointmentDao: AppointmentDao = AppDatabase.getDatabase(application).appointmentDao()

    // _appointment ahora es de tipo ModelAppointment
    private val _appointment = MutableLiveData<ModelAppointment?>()
    val appointment: LiveData<ModelAppointment?> = _appointment

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    private val _navigateToEdit = MutableLiveData<Int?>()
    val navigateToEdit: LiveData<Int?> = _navigateToEdit

    fun init(appointmentId: Int) {
        if (appointmentId < 0) {
            _appointment.value = null
            return
        }
        loadAppointmentDetail(appointmentId)
    }

    // Función de mapeo de Entidad a Modelo
    private fun mapEntityToModel(entity: EntityAppointment?): ModelAppointment? {
        return entity?.let {
            ModelAppointment(
                id = it.id,
                petName = it.petName,
                breed = it.breed,
                ownerName = it.ownerName,
                ownerPhone = it.ownerPhone,
                symptoms = it.symptoms,
                petImageUrl = it.petImageUrl
                // Asegúrate de que todos los campos necesarios para ModelAppointment se mapeen desde EntityAppointment
            )
        }
    }

    // Función de mapeo de Modelo a Entidad (necesaria para borrar si el DAO espera Entidad)
    private fun mapModelToEntity(model: ModelAppointment?): EntityAppointment? {
        return model?.let {
            EntityAppointment(
                id = it.id,
                petName = it.petName,
                breed = it.breed,
                ownerName = it.ownerName,
                ownerPhone = it.ownerPhone,
                symptoms = it.symptoms,
                petImageUrl = it.petImageUrl
                // Asegúrate de que todos los campos necesarios para EntityAppointment se mapeen desde ModelAppointment
            )
        }
    }


    private fun loadAppointmentDetail(id: Int) {
        viewModelScope.launch {
            val entityAppointmentFromDb: EntityAppointment? = appointmentDao.getAppointmentById(id)
            // Mapeamos la entidad al modelo antes de postear al LiveData
            val modelAppointment = mapEntityToModel(entityAppointmentFromDb)
            _appointment.postValue(modelAppointment)
        }
    }

    fun onDeleteClicked() {
        _appointment.value?.let { currentModelAppointment -> // currentModelAppointment es ModelAppointment
            viewModelScope.launch {
                // Mapeamos el modelo de vuelta a entidad para la operación del DAO
                val entityToDelete = mapModelToEntity(currentModelAppointment)
                if (entityToDelete != null) {
                    appointmentDao.deleteAppointment(entityToDelete) // El DAO espera EntityAppointment
                    _navigateToHome.postValue(true)
                } else {
                    // Manejar el caso donde el mapeo falla, aunque es improbable si currentModelAppointment no es null
                    _navigateToHome.postValue(true) // O mostrar un error
                }
            }
        } ?: run {
            _navigateToHome.value = true
        }
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun onEditClicked() {
        _navigateToEdit.value = _appointment.value?.id // id debería ser el mismo en modelo y entidad
    }

    fun onEditNavigated() {
        _navigateToEdit.value = null
    }
}