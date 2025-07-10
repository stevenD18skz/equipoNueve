package com.example.dogapp.detailappointment

// Importaciones necesarias para UI, navegación, binding y carga de imágenes
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentAppointmentDetailBinding
import com.example.dogapp.model.Appointment

/**
 * Fragmento que muestra el detalle de una cita.
 * Permite visualizar la información de la cita seleccionada, navegar hacia atrás o editar la cita.
 */
class AppointmentDetailFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout de forma segura
    private var _binding: FragmentAppointmentDetailBinding? = null
    private val binding get() = _binding!!

    // ViewModel inyectado para manejar la lógica y datos del detalle
    private val viewModel: AppointmentDetailViewModel by viewModels()

    // Safe Args para recibir el ID de la cita a mostrar
    private val args: AppointmentDetailFragmentArgs by navArgs()

    /**
     * Infla el layout del fragmento usando ViewBinding
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se ejecuta después de que la vista ha sido creada. Inicializa el ViewModel y listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializa el ViewModel con el ID recibido por argumentos
        viewModel.init(args.appointmentId)
        observeViewModel()      // Observa cambios en el ViewModel
        setupClickListeners()   // Configura listeners de botones
    }

    /**
     * Observa los LiveData del ViewModel para actualizar la UI o navegar
     */
    private fun observeViewModel() {
        viewModel.appointment.observe(viewLifecycleOwner) { appointment ->
            if (appointment != null) {
                bindAppointmentData(appointment)
            } else {
                Toast.makeText(requireContext(), "Cita no encontrada", Toast.LENGTH_LONG).show()
                findNavController().navigate(
                    AppointmentDetailFragmentDirections
                        .actionAppointmentDetailFragmentToHomeFragment()
                )
            }
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) { goHome ->
            if (goHome) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.appointment_deleted_message),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(
                    AppointmentDetailFragmentDirections
                        .actionAppointmentDetailFragmentToHomeFragment()
                )
                viewModel.onHomeNavigated()
            }
        }

        viewModel.navigateToEdit.observe(viewLifecycleOwner) { editId ->
            editId?.let {
                // Navegamos pasando el mismo appointmentId
                findNavController().navigate(
                    AppointmentDetailFragmentDirections
                        .actionAppointmentDetailFragmentToEditAppointmentFragment(it)
                )
                viewModel.onEditNavigated()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindAppointmentData(appointment: Appointment) {
        binding.textViewToolbarTitle.text = appointment.petName
        binding.imageViewPetDetail.load(appointment.petImageUrl) {
            placeholder(R.drawable.ic_pet_placeholder)
            error(R.drawable.ic_pet_placeholder)
        }
        binding.textViewAppointmentTurnDetail.text = "#${appointment.id}"
        binding.textViewBreedDetail.text = appointment.breed
        binding.textViewSymptomDetail.text = appointment.symptoms
        binding.textViewOwnerNameDetail.text = appointment.ownerName
        binding.textViewPhoneNumberDetail.text = appointment.ownerPhone
    }

    private fun setupClickListeners() {
        binding.imageViewBackButton.setOnClickListener {
            findNavController().navigate(
                AppointmentDetailFragmentDirections
                    .actionAppointmentDetailFragmentToHomeFragment()
            )
        }
        binding.fabDeleteAppointment.setOnClickListener {
            viewModel.onDeleteClicked()
        }
        binding.fabEditAppointment.setOnClickListener {
            viewModel.onEditClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
