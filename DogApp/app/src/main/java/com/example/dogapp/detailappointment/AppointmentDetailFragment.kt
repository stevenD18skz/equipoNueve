package com.example.dogapp.detail // Ajusta tu package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // para by viewModels()
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs // para los argumentos de navegación
import coil.load
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentAppointmentDetailBinding
import com.example.dogapp.model.Appointment

class AppointmentDetailFragment : Fragment() {

    private var _binding: FragmentAppointmentDetailBinding? = null
    private val binding get() = _binding!!

    // ViewModel para este fragmento, inyectando SavedStateHandle automáticamente
    private val viewModel: AppointmentDetailViewModel by viewModels()

    // Delegado para obtener los argumentos de navegación de forma segura
    private val args: AppointmentDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // El ID de la cita ya está disponible a través de viewModel.init (usando SavedStateHandle)
        // o directamente a través de args.appointmentId

        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel() {
        viewModel.appointment.observe(viewLifecycleOwner) { appointment ->
            appointment?.let {
                bindAppointmentData(it)
            } ?: run {
                // Manejar el caso donde la cita no se encuentra (ej. mostrar error o navegar atrás)
                Toast.makeText(requireContext(), "Cita no encontrada", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                Toast.makeText(requireContext(), getString(R.string.appointment_deleted_message), Toast.LENGTH_SHORT).show()
                // Navegar a HU 2.0 (Home)
                findNavController().navigate(R.id.action_appointmentDetailFragment_to_homeFragment)
                viewModel.onNavigateToHomeComplete()
            }
        }

        viewModel.navigateToEdit.observe(viewLifecycleOwner) { appointmentIdToEdit ->
            appointmentIdToEdit?.let {
                Toast.makeText(requireContext(), getString(R.string.navigate_to_edit_appointment, it), Toast.LENGTH_SHORT).show()
                // Navegar a HU 5.0 (Editar Cita) con el ID
                val action = AppointmentDetailFragmentDirections.actionAppointmentDetailFragmentToEditAppointmentFragment(it)
                findNavController().navigate(action)
                viewModel.onNavigateToEditComplete()
            }
        }
    }

    private fun bindAppointmentData(appointment: Appointment) {
        binding.textViewToolbarTitle.text = appointment.petName // Criterio 1: Nombre mascota en toolbar
        binding.imageViewPetDetail.load(appointment.petImageUrl) { // Criterio 2: Imagen
            placeholder(R.drawable.ic_pet_placeholder)
            error(R.drawable.ic_pet_placeholder)
        }
        // Criterio 3: Detalles en la tarjeta inferior
        binding.textViewAppointmentTurnDetail.text = "#${appointment.id}"
        binding.textViewBreedDetail.text = appointment.breed
        binding.textViewSymptomDetail.text = appointment.symptoms
        binding.textViewOwnerNameDetail.text = appointment.ownerName
        binding.textViewPhoneNumberDetail.text = appointment.ownerPhone
    }

    private fun setupClickListeners() {
        // Criterio 1: Flecha de atrás
        binding.imageViewBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentDetailFragment_to_homeFragment)
            // o findNavController().popBackStack() si la acción de volver ya está definida para eso.
        }

        // Criterio 4: FAB Eliminar
        binding.fabDeleteAppointment.setOnClickListener {
            viewModel.onDeleteClicked()
        }

        // Criterio 5: FAB Editar
        binding.fabEditAppointment.setOnClickListener {
            viewModel.onEditClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}