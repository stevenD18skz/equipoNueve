package com.example.dogapp.home.adapter

// Importaciones necesarias para RecyclerView, binding y carga de imágenes
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemAppointmentBinding
import com.example.dogapp.database.entity.Appointment

/**
 * Adaptador para mostrar la lista de citas en el RecyclerView de la pantalla principal.
 * Usa ListAdapter y DiffUtil para optimizar el renderizado y actualización de la lista.
 * El callback onItemClicked permite manejar el click en cada ítem desde el fragmento.
 */
class AppointmentAdapter(private val onItemClicked: (Appointment) -> Unit) :
    ListAdapter<Appointment, AppointmentAdapter.AppointmentViewHolder>(DiffCallback) {

    /**
     * Infla el layout de cada ítem y crea el ViewHolder correspondiente
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    /**
     * Asocia los datos de la cita al ViewHolder y configura el click listener
     */
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val currentAppointment = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentAppointment)
        }
        holder.bind(currentAppointment)
    }

    /**
     * ViewHolder que encapsula la lógica de binding de datos para cada cita
     */
    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Asigna los datos de la cita a las vistas correspondientes
         */
        fun bind(appointment: Appointment) {
            binding.textViewPetName.text = appointment.petName
            binding.textViewSymptom.text = appointment.symptoms
            binding.textViewAppointmentTurn.text = "#${appointment.id}"

            // Carga la imagen de la mascota usando Coil
            binding.imageViewPet.load(appointment.petImageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_pet_placeholder)
                error(R.drawable.ic_pet_placeholder)
            }
        }
    }

    /**
     * DiffUtil para optimizar la actualización de la lista de citas
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            // La data class compara todos los campos automáticamente
            return oldItem == newItem
        }
    }
}

