package com.example.dogapp.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemAppointmentBinding
import com.example.dogapp.database.entity.Appointment


class AppointmentAdapter(private val onItemClicked: (Appointment) -> Unit) :
    ListAdapter<Appointment, AppointmentAdapter.AppointmentViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val currentAppointment = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentAppointment)
        }
        holder.bind(currentAppointment)
    }

    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
