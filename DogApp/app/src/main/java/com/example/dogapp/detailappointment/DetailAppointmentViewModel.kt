package com.example.dogapp.detailappointment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogapp.model.Appointment as ModelAppointment
import com.example.dogapp.database.entity.Appointment as EntityAppointment
import com.example.dogapp.database.AppDatabase
import com.example.dogapp.database.dao.AppointmentDao
import kotlinx.coroutines.launch

class AppointmentDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appointmentDao: AppointmentDao = AppDatabase.getDatabase(application).appointmentDao()


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
            )
        }
    }

    // Función de mapeo de Modelo a Entidad
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
            )
        }
    }


    private fun loadAppointmentDetail(id: Int) {
        viewModelScope.launch {
            val entityAppointmentFromDb: EntityAppointment? = appointmentDao.getAppointmentById(id)
            val modelAppointment = mapEntityToModel(entityAppointmentFromDb)
            _appointment.postValue(modelAppointment)
        }
    }

    fun onDeleteClicked() {
        _appointment.value?.let { currentModelAppointment ->
            viewModelScope.launch {
                val entityToDelete = mapModelToEntity(currentModelAppointment)
                if (entityToDelete != null) {
                    appointmentDao.deleteAppointment(entityToDelete)
                    _navigateToHome.postValue(true)
                } else {
                    _navigateToHome.postValue(true)
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
        _navigateToEdit.value = _appointment.value?.id
    }

    fun onEditNavigated() {
        _navigateToEdit.value = null
    }
}