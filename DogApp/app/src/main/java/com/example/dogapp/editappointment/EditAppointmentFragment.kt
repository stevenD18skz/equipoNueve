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
// Asegúrate de que el ViewModel se importe correctamente si está en el mismo paquete
// import com.example.dogapp.editappointment.EditAppointmentViewModel

class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentEditBinding? = null
    private val binding get() = _binding!!

    // El ViewModel se inicializa y obtiene el appointmentId a través de SavedStateHandle
    private val viewModel: EditAppointmentViewModel by viewModels()
    // args se usa para obtener el appointmentId si el ViewModel NO usara SavedStateHandle,
    // pero como sí lo usa, args.appointmentId no es estrictamente necesario pasarlo al ViewModel.
    // Sin embargo, tenerlo aquí no hace daño si lo usaras para alguna otra lógica inicial en el Fragment.
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

        // YA NO ES NECESARIO LLAMAR A viewModel.init()
        // El ViewModel carga los datos en su propio bloque init usando SavedStateHandle
        // y el appointmentId que le llega a través de la navegación.
        // viewModel.init(args.appointmentId) // <<<--- ELIMINA ESTA LÍNEA

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
        // Estos observadores se activarán cuando el ViewModel cargue los datos
        viewModel.petName.observe(viewLifecycleOwner) { name ->
            if (binding.etEditPetName.text.toString() != name) {
                binding.etEditPetName.setText(name)
            }
        }
        viewModel.breed.observe(viewLifecycleOwner) { breed ->
            val field = binding.tilEditBreed.editText as? AutoCompleteTextView
            if (field?.text.toString() != breed) {
                // El segundo argumento 'false' en setText para AutoCompleteTextView
                // evita que se active el filtro del adapter inmediatamente,
                // lo cual es bueno al pre-rellenar.
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
            if (goHome == true) { // Es buena práctica comparar con true para LiveData de eventos
                Toast.makeText(
                    requireContext(),
                    getString(R.string.edit_appointment_success),
                    Toast.LENGTH_SHORT
                ).show()
                // Asegúrate que esta acción exista en tu nav_graph.xml
                findNavController().navigate(
                    EditAppointmentFragmentDirections
                        .actionEditAppointmentFragmentToHomeFragmentAfterEdit() // Nombre de acción corregido
                )
                viewModel.onHomeNavigated() // Resetea el evento
            }
        }

        // Navegar al detalle si presionan atrás en toolbar
        viewModel.navigateToDetail.observe(viewLifecycleOwner) { id ->
            id?.let {
                // Asegúrate que esta acción exista en tu nav_graph.xml
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
            viewModel.onToolbarBackClicked() // Llamar al método correcto del ViewModel
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
