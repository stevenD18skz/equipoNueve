package com.example.dogapp.home // Ajusta tu package name

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
import com.example.dogapp.model.Appointment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        // Criterio 4 (HU 2.0): Listener para el FAB
        binding.fabAddAppointment.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.navigate_to_new_appointment), Toast.LENGTH_SHORT).show()
            // Navega a la pantalla de nueva cita (HU 3.0 - placeholder)
            // Asegúrate que esta acción exista en nav_graph.xml
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    private fun setupRecyclerView() {
        // El lambda para el click recibe el objeto Appointment (del mock)
        appointmentAdapter = AppointmentAdapter { appointment ->
            // Criterio 5 (HU 2.0): Al dar clic en un ítem, llevar a HU 4.0 (Detalle)
            Toast.makeText(requireContext(), getString(R.string.navigate_to_appointment_detail, appointment.id), Toast.LENGTH_SHORT).show()

            // --- Forma Simplificada de Navegar sin Safe Args por ahora ---
            // Creamos un Bundle para pasar el ID del appointment
            val bundle = Bundle().apply {
                putInt("appointmentId", appointment.id) // "appointmentId" debe coincidir con el nombre del argumento en nav_graph
            }
            // Navegamos a la pantalla de detalle (HU 4.0 - placeholder)
            // Asegúrate que esta acción y el argumento "appointmentId" existan en nav_graph.xml para el destino appointmentDetailFragment
            findNavController().navigate(R.id.action_homeFragment_to_appointmentDetailFragment, bundle)

            // Si Safe Args está configurado y HomeFragmentDirections existe, podrías usar:
            // val action = HomeFragmentDirections.actionHomeFragmentToAppointmentDetailFragment(appointment.id)
            // findNavController().navigate(action)
            // Pero si te da errores, usa la forma con Bundle por ahora.
        }

        binding.recyclerViewAppointments.apply {
            adapter = appointmentAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true) // Optimización
        }
    }

    private fun observeViewModel() {
        homeViewModel.allAppointments.observe(viewLifecycleOwner) { appointments ->
            appointments?.let {
                appointmentAdapter.submitList(it) // El adapter actualiza la lista

                // Muestra/oculta mensaje de lista vacía
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