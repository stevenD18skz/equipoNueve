package com.example.dogapp.detailappointment

// Importaciones necesarias para ViewModel, LiveData, corrutinas y acceso a base de datos
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

/**
 * ViewModel para el detalle de una cita.
 * Gestiona la carga, transformación y navegación desde el detalle de la cita.
 */
class AppointmentDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    // DAO para acceder a la base de datos de citas
    private val appointmentDao: AppointmentDao = AppDatabase.getDatabase(application).appointmentDao()

    // LiveData con la cita actual (modelo de presentación)
    private val _appointment = MutableLiveData<ModelAppointment?>()
    val appointment: LiveData<ModelAppointment?> = _appointment

    // LiveData para navegar a Home
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    // LiveData para navegar a la edición de la cita
    private val _navigateToEdit = MutableLiveData<Int?>()
    val navigateToEdit: LiveData<Int?> = _navigateToEdit

    /**
     * Inicializa el ViewModel cargando el detalle de la cita por ID.
     * Si el ID es negativo, se establece la cita en null.
     */
    fun init(appointmentId: Int) {
        if (appointmentId < 0) {
            _appointment.value = null
            return
        }
        loadAppointmentDetail(appointmentId)
    }

    /**
     * Mapea una entidad de base de datos a un modelo de presentación.
     * Se utiliza para transformar la entidad en un objeto más adecuado para la presentación.
     */
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

    /**
     * Mapea un modelo de presentación a una entidad de base de datos.
     * Se utiliza para transformar el modelo en un objeto más adecuado para la base de datos.
     */
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