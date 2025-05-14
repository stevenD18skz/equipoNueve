package com.example.dogapp.newappointment // Ajusta tu package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentNewAppointmentBinding

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentNewAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewAppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAppointmentBinding.inflate(inflater, container, false)
        // Vinculamos el ViewModel al layout (si usas data binding)
        // binding.viewModel = viewModel
        // binding.lifecycleOwner = viewLifecycleOwner // Importante para observar LiveData en el layout
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
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, breeds)
            (binding.tilBreed.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
        // Actualizar ViewModel cuando el texto cambia
        (binding.tilBreed.editText as? AutoCompleteTextView)?.doOnTextChanged { text, _, _, _ ->
            viewModel.breed.value = text.toString()
        }
    }

    private fun setupSymptomsSpinner() {
        // Crear adapter desde el array de strings
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.symptom_array,
            android.R.layout.simple_spinner_item // Layout por defecto para el ítem seleccionado
        ).also { adapter ->
            // Especificar layout para el dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSymptoms.adapter = adapter
        }

        // Listener para cuando se selecciona un ítem
        binding.spinnerSymptoms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Obtener el texto seleccionado y actualizar ViewModel
                // Ignoramos la posición 0 si es el hint/texto por defecto
                val selected = parent.getItemAtPosition(position).toString()
                viewModel.selectedSymptom.value = if (position > 0) selected else ""
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.selectedSymptom.value = ""
            }
        }
    }


    private fun setupInputListeners() {
        // Usamos doOnTextChanged para simplificar
        binding.etPetName.doOnTextChanged { text, _, _, _ ->
            viewModel.petName.value = text.toString()
        }
        binding.etOwnerName.doOnTextChanged { text, _, _, _ ->
            viewModel.ownerName.value = text.toString()
        }
        binding.etOwnerPhone.doOnTextChanged { text, _, _, _ ->
            viewModel.ownerPhone.value = text.toString()
        }
        // El de Raza (Breed) ya se actualiza en setupBreedAutoComplete
    }

    private fun observeViewModel() {
        // Observar estado del botón Guardar
        viewModel.isSaveButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.buttonSaveAppointment.isEnabled = isEnabled
            // Criterio 9: Cambiar estilo del texto si está habilitado (usando selector o programático)
            if (isEnabled) {
                binding.buttonSaveAppointment.setTypeface(null, android.graphics.Typeface.BOLD) // O un estilo
            } else {
                binding.buttonSaveAppointment.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }

        // Observar evento para navegar al Home
        viewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                Toast.makeText(requireContext(), getString(R.string.save_appointment_success), Toast.LENGTH_SHORT).show()
                // Navegar de vuelta a Home (asegúrate que la acción exista en nav_graph)
                findNavController().navigate(R.id.action_newAppointmentFragment_to_homeFragment)
                viewModel.onNavigationComplete() // Resetea el evento
            }
        }

        // Observar evento para mostrar mensaje de error de síntoma
        viewModel.showSelectSymptomMessage.observe(viewLifecycleOwner) { show ->
            if (show) {
                Toast.makeText(requireContext(), getString(R.string.error_select_symptom), Toast.LENGTH_SHORT).show()
                viewModel.onShowSelectSymptomMessageComplete() // Resetea el evento
            }
        }
    }

    private fun setupClickListeners() {
        // Botón Guardar
        binding.buttonSaveAppointment.setOnClickListener {
            val defaultSymptom = resources.getStringArray(R.array.symptom_array)[0] // Obtener texto hint
            viewModel.saveAppointment(defaultSymptom)
        }

        // Botón Atrás en la Toolbar
        binding.imageViewNewBackButton.setOnClickListener {
            // Navegar a Home (o simplemente popBackStack)
            findNavController().navigate(R.id.action_newAppointmentFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }
}