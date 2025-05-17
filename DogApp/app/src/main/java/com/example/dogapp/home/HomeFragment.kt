package com.example.dogapp.home

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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // homeViewModel (home_view_model_room) ya provee LiveData<List<com.example.dogapp.database.entity.Appointment>>
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
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    private fun setupRecyclerView() {

        appointmentAdapter = AppointmentAdapter { appointment ->
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
