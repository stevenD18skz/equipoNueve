package com.example.dogapp.editappointment

// Importaciones necesarias para UI, navegación y binding
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

/**
 * Fragmento para editar una cita existente.
 * Gestiona el formulario de edición, la validación de campos y la interacción con el ViewModel.
 */
class EditAppointmentFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout de forma segura
    private var _binding: FragmentAppointmentEditBinding? = null
    private val binding get() = _binding!!

    // ViewModel para manejar la lógica de edición
    private val viewModel: EditAppointmentViewModel by viewModels()
    // Safe Args para recibir argumentos de navegación
    private val args: EditAppointmentFragmentArgs by navArgs()

    /**
     * Infla el layout del fragmento usando ViewBinding
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se ejecuta después de que la vista ha sido creada. Configura listeners y observadores.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBreedAutoComplete()   // Configura el autocompletado de razas
        setupInputListeners()      // Escucha cambios en los campos de texto
        observeViewModel()         // Observa cambios en el ViewModel
        setupClickListeners()      // Configura los clicks de los botones
    }

    /**
     * Configura el campo de autocompletado para la raza usando la lista del ViewModel
     */
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

    /**
     * Configura los listeners para los campos de entrada del formulario
     */
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
            // Solo habilita el botón si no se está cargando una imagen Y los campos son válidos
            val currentlyFetching = viewModel.isFetchingImage.value ?: false
            binding.buttonUpdateAppointment.isEnabled = enabled && !currentlyFetching
            binding.buttonUpdateAppointment.setTypeface(
                null,
                if (enabled && !currentlyFetching) Typeface.BOLD else Typeface.NORMAL
            )
        }

        // Observar estado de carga de imagen
        viewModel.isFetchingImage.observe(viewLifecycleOwner) { isFetching ->
            binding.progressBarEditAppointment.visibility = if (isFetching) View.VISIBLE else View.GONE
            // Deshabilitar el botón mientras se carga la imagen
            val fieldsValid = viewModel.isEditButtonEnabled.value ?: false
            binding.buttonUpdateAppointment.isEnabled = fieldsValid && !isFetching
            if (isFetching) {
                binding.buttonUpdateAppointment.text = "Guardando..."
            } else {
                binding.buttonUpdateAppointment.text = getString(R.string.button_edit_appointment)
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
            if (goHome == true) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.edit_appointment_success),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(
                    EditAppointmentFragmentDirections
                        .actionEditAppointmentFragmentToHomeFragmentAfterEdit()
                )
                viewModel.onHomeNavigated() // Resetea el evento
            }
        }

        // Navegar al detalle si presionan atrás en toolbar
        viewModel.navigateToDetail.observe(viewLifecycleOwner) { id ->
            id?.let {
                findNavController().navigate(
                    EditAppointmentFragmentDirections
                        .actionEditAppointmentFragmentToAppointmentDetailFragment(it)
                )
                viewModel.onDetailNavigated() // Resetea el evento
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonUpdateAppointment.setOnClickListener {
            viewModel.updateAppointment()
        }
        binding.imageViewEditBackButton.setOnClickListener {
            viewModel.onToolbarBackClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
