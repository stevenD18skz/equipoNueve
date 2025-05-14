package com.example.dogapp.edit // Ajusta tu package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogapp.databinding.FragmentEditAppointmentBinding // Se generará de fragment_edit_appointment.xml

class EditAppointmentFragment : Fragment() {
    private var _binding: FragmentEditAppointmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appointmentId = arguments?.getInt("appointmentId", -1) ?: -1
        // Puedes usar el ID aquí si lo necesitas
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}