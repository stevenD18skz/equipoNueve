package com.example.dogapp.home.adapter // Ajusta tu package name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemAppointmentBinding
// CAMBIO IMPORTANTE: Usar la entidad de Room
import com.example.dogapp.database.entity.Appointment

// CAMBIO IMPORTANTE: El lambda ahora recibe la entidad de Room
class AppointmentAdapter(private val onItemClicked: (Appointment) -> Unit) :
    ListAdapter<Appointment, AppointmentAdapter.AppointmentViewHolder>(DiffCallback) { // Appointment es ahora la entidad de Room

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val currentAppointment = getItem(position) // Esto será un com.example.dogapp.database.entity.Appointment
        holder.itemView.setOnClickListener {
            onItemClicked(currentAppointment)
        }
        holder.bind(currentAppointment)
    }

    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // CAMBIO IMPORTANTE: El parámetro es la entidad de Room
        fun bind(appointment: Appointment) {
            binding.textViewPetName.text = appointment.petName
            binding.textViewSymptom.text = appointment.symptoms
            binding.textViewAppointmentTurn.text = "#${appointment.id}"

            binding.imageViewPet.load(appointment.petImageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_pet_placeholder)
                error(R.drawable.ic_pet_placeholder)
            }
        }
    }

    // CAMBIO IMPORTANTE: DiffUtil ahora compara la entidad de Room
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
