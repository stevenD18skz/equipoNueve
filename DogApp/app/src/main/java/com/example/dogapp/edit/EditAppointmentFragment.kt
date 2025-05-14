package com.example.dogapp.edit // Ajusta tu package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentEditAppointmentBinding

class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentEditAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditAppointmentViewModel by viewModels()
    private val args: EditAppointmentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // El ID ya es accesible a través de args.appointmentId
        // y el ViewModel lo carga en su init.

        setupInputListeners()
        setupBreedAutoComplete()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupBreedAutoComplete() {
        viewModel.breedList.observe(viewLifecycleOwner) { breeds ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, breeds)
            (binding.tilEditBreed.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
        (binding.tilEditBreed.editText as? AutoCompleteTextView)?.doOnTextChanged { text, _, _, _ ->
            viewModel.breed.value = text.toString()
        }
    }

    private fun setupInputListeners() {
        binding.etEditPetName.doOnTextChanged { text, _, _, _ ->
            viewModel.petName.value = text.toString()
        }
        binding.etEditOwnerName.doOnTextChanged { text, _, _, _ ->
            viewModel.ownerName.value = text.toString()
        }
        binding.etEditOwnerPhone.doOnTextChanged { text, _, _, _ ->
            viewModel.ownerPhone.value = text.toString()
        }
    }

    private fun observeViewModel() {
        // Observar los datos originales para pre-rellenar una vez
        viewModel.petName.observe(viewLifecycleOwner) { name ->
            // Evitar sobreescribir si el usuario ya está editando
            if (binding.etEditPetName.text.toString() != name) {
                binding.etEditPetName.setText(name)
            }
        }
        viewModel.breed.observe(viewLifecycleOwner) { breed ->
            if ((binding.tilEditBreed.editText as? AutoCompleteTextView)?.text.toString() != breed) {
                (binding.tilEditBreed.editText as? AutoCompleteTextView)?.setText(breed, false) // false para no filtrar de nuevo
            }
        }
        viewModel.ownerName.observe(viewLifecycleOwner) { owner ->
            if (binding.etEditOwnerName.text.toString() != owner) {
                binding.etEditOwnerName.setText(owner)
            }
        }
        viewModel.ownerPhone.observe(viewLifecycleOwner) { phone ->
            if (binding.etEditOwnerPhone.text.toString() != phone) {
                binding.etEditOwnerPhone.setText(phone)
            }
        }


        // Observar estado del botón Editar
        viewModel.isEditButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.buttonUpdateAppointment.isEnabled = isEnabled
            if (isEnabled) { // Criterio 7 y 8
                binding.buttonUpdateAppointment.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                binding.buttonUpdateAppointment.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }

        // Observar evento para navegar al Home
        viewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                Toast.makeText(requireContext(), getString(R.string.edit_appointment_success), Toast.LENGTH_SHORT).show()
                // Criterio 8: Navegar a Home (HU 2.0)
                findNavController().navigate(R.id.action_editAppointmentFragment_to_homeFragment_after_edit)
                viewModel.onNavigationToHomeComplete()
            }
        }

        // Observar evento para navegar al Detalle (botón atrás de la toolbar)
        viewModel.navigateToDetail.observe(viewLifecycleOwner) { appointmentId ->
            appointmentId?.let {
                // Criterio 1: Navegar a Detalle (HU 4.0)
                val action = EditAppointmentFragmentDirections.actionEditAppointmentFragmentToAppointmentDetailFragment(it)
                findNavController().navigate(action)
                viewModel.onNavigationToDetailComplete()
            }
        }
    }

    private fun setupClickListeners() {
        // Botón Editar Cita
        binding.buttonUpdateAppointment.setOnClickListener {
            viewModel.updateAppointment()
        }

        // Botón Atrás en la Toolbar
        binding.imageViewEditBackButton.setOnClickListener {
            viewModel.onToolbarBackClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}