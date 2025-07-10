package com.example.dogapp.home

// Importaciones necesarias para UI, navegación y binding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentHomeBinding
import com.example.dogapp.home.adapter.AppointmentAdapter

/**
 * Fragmento principal que muestra la lista de citas agendadas.
 * Permite navegar al detalle o agregar una nueva cita.
 */
class HomeFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout de forma segura
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    // ViewModel para manejar la lógica de la pantalla principal
    private val homeViewModel: HomeViewModel by viewModels()
    // Adaptador para el RecyclerView de citas
    private lateinit var appointmentAdapter: AppointmentAdapter

    /**
     * Infla el layout del fragmento usando ViewBinding
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se ejecuta después de que la vista ha sido creada. Configura la lista y los listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()   // Inicializa la lista de citas
        observeViewModel()    // Observa los cambios en el ViewModel

        // Listener para el botón flotante (FAB) que navega a la pantalla de nueva cita
        binding.fabAddAppointment.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.navigate_to_new_appointment), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    /**
     * Configura el RecyclerView y su adaptador, y define el listener para cada ítem
     */
    private fun setupRecyclerView() {

        appointmentAdapter = AppointmentAdapter { appointment ->
            // Al pulsar una cita, muestra un toast y navega al detalle
            Toast.makeText(requireContext(), getString(R.string.navigate_to_appointment_detail, appointment.id), Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putInt("appointmentId", appointment.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_appointmentDetailFragment, bundle)
        }

        binding.recyclerViewAppointments.apply {
            adapter = appointmentAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        homeViewModel.allAppointments.observe(viewLifecycleOwner) { appointments ->
            appointments?.let {
                appointmentAdapter.submitList(it)

                binding.recyclerViewAppointments.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                binding.textViewEmptyList.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
