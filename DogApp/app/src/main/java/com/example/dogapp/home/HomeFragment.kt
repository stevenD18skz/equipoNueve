package com.example.dogapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Desactivamos el FAB para que no navegue a nueva cita:
        binding.fabAddAppointment.apply {
            isEnabled = false
            hide()  // o `visibility = View.GONE` si prefieres ocultarlo
        }

        // Si tenías un adapter con click listener para ir a detalle,
        // simplemente no lo registres aquí, o deja la lista en modo sólo lectura.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
