package com.example.dogapp.newappointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentAppointmentNewBinding

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentNewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewAppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBreedAutoComplete()
        setupSymptomsSpinner()
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
            (binding.tilBreed.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
        (binding.tilBreed.editText as? AutoCompleteTextView)
            ?.doOnTextChanged { text, _, _, _ ->
                viewModel.breed.value = text.toString()
            }
    }

    private fun setupSymptomsSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.symptom_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSymptoms.adapter = adapter
        }
        binding.spinnerSymptoms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selected = parent.getItemAtPosition(position).toString()
                viewModel.selectedSymptom.value =
                    if (position > 0) selected else ""
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.selectedSymptom.value = ""
            }
        }
    }

    private fun setupInputListeners() {
        binding.etPetName.doOnTextChanged { text, _, _, _ ->
            viewModel.petName.value = text.toString()
        }
        binding.etOwnerName.doOnTextChanged { text, _, _, _ ->
            viewModel.ownerName.value = text.toString()
        }
        binding.etOwnerPhone.doOnTextChanged { text, _, _, _ ->
            viewModel.ownerPhone.value = text.toString()
        }
    }

    private fun observeViewModel() {
        viewModel.isSaveButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.buttonSaveAppointment.isEnabled = enabled
            binding.buttonSaveAppointment.setTypeface(
                null,
                if (enabled) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL
            )
        }

        viewModel.showSelectSymptomMessage.observe(viewLifecycleOwner) { show ->
            if (show) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_select_symptom),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.onShowSelectSymptomMessageComplete()
            }
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) { goHome ->
            if (goHome) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.save_appointment_success),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(
                    NewAppointmentFragmentDirections
                        .actionNewAppointmentFragmentToHomeFragment()
                )
                viewModel.onNavigationComplete()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonSaveAppointment.setOnClickListener {
            val defaultHint = resources.getStringArray(R.array.symptom_array)[0]
            viewModel.saveAppointment(defaultHint)
        }
        binding.imageViewNewBackButton.setOnClickListener {
            findNavController().navigate(
                NewAppointmentFragmentDirections
                    .actionNewAppointmentFragmentToHomeFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
