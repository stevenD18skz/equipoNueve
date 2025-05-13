package com.example.dogapp.home // Reemplaza con tu package name

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogapp.databinding.FragmentHomeBinding // Asume que crearás este layout

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Criterio 1 de HU 2.0: Al estar en esta ventana y si por alguna razón se usa el ícono de atrás propio del teléfono no
        // debe dirigirlo al login, debe enviarlo al escritorio del celular.
        // Esto se maneja con app:popUpTo e app:popUpToInclusive="true" en la acción de nav_graph.xml

        // Por ahora, solo un texto simple.
        binding.textViewHome.text = "Bienvenido al Home Administrador de Citas (HU 2.0)"
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}