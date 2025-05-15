// src/main/java/com/example/dogapp/editappointment/EditAppointmentFragment.kt
package com.example.dogapp.editappointment

import android.graphics.Typeface
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
import com.example.dogapp.databinding.FragmentAppointmentEditBinding



class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditAppointmentViewModel by viewModels()
    private val args: EditAppointmentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa ViewModel con el ID de Safe Args
        viewModel.init(args.appointmentId)

        setupBreedAutoComplete()
        setupInputListeners()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupBreedAutoComplete() {
        viewModel.breedList.observe(viewLifecycleOwner) { breeds ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                breeds
            )
            (binding.tilEditBreed.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
        (binding.tilEditBreed.editText as? AutoCompleteTextView)
            ?.doOnTextChanged { text, _, _, _ ->
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
        // Rellenar campos iniciales
        viewModel.petName.observe(viewLifecycleOwner) { name ->
            if (binding.etEditPetName.text.toString() != name) {
                binding.etEditPetName.setText(name)
            }
        }
        viewModel.breed.observe(viewLifecycleOwner) { breed ->
            val field = binding.tilEditBreed.editText as? AutoCompleteTextView
            if (field?.text.toString() != breed) {
                field?.setText(breed, false)
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

        // Habilitar/deshabilitar botón
        viewModel.isEditButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.buttonUpdateAppointment.isEnabled = enabled
            binding.buttonUpdateAppointment.setTypeface(
                null,
                if (enabled) Typeface.BOLD else Typeface.NORMAL
            )
        }

        // Navegar a Home tras guardar
        viewModel.navigateToHome.observe(viewLifecycleOwner) { goHome ->
            if (goHome) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.edit_appointment_success),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(
                    EditAppointmentFragmentDirections
                        .actionEditAppointmentFragmentToHomeFragment()
                )
                viewModel.onHomeNavigated()
            }
        }

        // Navegar al detalle si presionan atrás en toolbar
        viewModel.navigateToDetail.observe(viewLifecycleOwner) { id ->
            id?.let {
                findNavController().navigate(
                    EditAppointmentFragmentDirections
                        .actionEditAppointmentFragmentToAppointmentDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonUpdateAppointment.setOnClickListener {
            viewModel.updateAppointment()
        }
        binding.imageViewEditBackButton.setOnClickListener {
            viewModel.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
