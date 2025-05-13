package com.example.dogapp.home.adapter // Ajusta tu package name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load // Para cargar imágenes (usaremos el placeholder)
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemAppointmentBinding
import com.example.dogapp.model.Appointment // Importa tu modelo simple

// El lambda onItemClicked ahora simplemente pasará el objeto Appointment completo
class AppointmentAdapter(private val onItemClicked: (Appointment) -> Unit) :
    ListAdapter<Appointment, AppointmentAdapter.AppointmentViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val currentAppointment = getItem(position)
        // Configura el click listener para todo el ítem
        holder.itemView.setOnClickListener {
            onItemClicked(currentAppointment)
        }
        holder.bind(currentAppointment)
    }

    // ViewHolder interno
    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) {
            binding.textViewPetName.text = appointment.petName
            binding.textViewSymptom.text = appointment.symptoms
            binding.textViewAppointmentTurn.text = "#${appointment.id}" // Muestra el ID del mock

            // Cargar imagen (placeholder por ahora)
            binding.imageViewPet.load(appointment.petImageUrl) { // petImageUrl será null en el mock
                crossfade(true)
                placeholder(R.drawable.ic_pet_placeholder) // Asegúrate de tener este drawable
                error(R.drawable.ic_pet_placeholder)       // Muestra placeholder si hay error o es null
            }
        }
    }

    // DiffUtil para eficiencia
    companion object DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem.id == newItem.id // Compara por ID único
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem == newItem // Compara el contenido completo del objeto
        }
    }
}