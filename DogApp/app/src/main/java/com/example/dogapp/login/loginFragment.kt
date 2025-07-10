package com.example.dogapp.login

// Importaciones necesarias para UI, navegación, biometría y binding
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentLoginBinding
import java.util.concurrent.Executor

/**
 * Fragmento de login que implementa autenticación biométrica y navegación a Home.
 * Gestiona el ciclo de vida, la inicialización de componentes biométricos y la observación del ViewModel.
 */
class LoginFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout de forma segura
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // ViewModel para manejar la lógica de autenticación y navegación
    private val loginViewModel: LoginViewModel by viewModels()

    // Executor y prompt para la autenticación biométrica
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    /**
     * Infla el layout del fragmento usando ViewBinding
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se ejecuta después de que la vista ha sido creada. Inicializa la biometría y listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el executor para la biometría
        executor = ContextCompat.getMainExecutor(requireContext())

        // Configura la autenticación biométrica
        setupBiometricAuth()

        // Listener para el botón de huella digital (Lottie)
        binding.lottieAnimationViewFingerprint.setOnClickListener {
            // Verifica si el dispositivo admite biometría y autentica
            checkBiometricSupportAndAuthenticate()
        }

        // Observa el LiveData para navegar a Home tras login exitoso
        loginViewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate == true) {
                // Muestra un mensaje de éxito y navega a Home
                Toast.makeText(requireContext(), getString(R.string.navigate_to_home_admin_citas), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                loginViewModel.onNavigationComplete() // Resetear el evento
            }
        }
    }

    /**
     * Configura la autenticación biométrica y establece los listeners para los eventos de autenticación.
     */
    private fun setupBiometricAuth() {
        // Crea un BiometricPrompt con el executor y el callback de autenticación
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Maneja el error de autenticación y muestra un mensaje al usuario
                    super.onAuthenticationError(errorCode, errString)
                    // Criterio 7: Mostrar mensaje de error (propio del dispositivo)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.biometric_auth_error, errString), Toast.LENGTH_SHORT
                    ).show()
                    Log.e("BIOMETRIC_LOGIN", "Authentication error: $errorCode - $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Criterio 7: Si la huella es correcta, dirigir a HU 2.0
                    Log.d("BIOMETRIC_LOGIN", "Authentication succeeded!")
                    loginViewModel.onAuthenticationSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Criterio 7: Si no es correcta, mostrará un mensaje de error
                    Toast.makeText(
                        requireContext(), getString(R.string.biometric_auth_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w("BIOMETRIC_LOGIN", "Authentication failed")
                }
            })

        // Criterio 5: Configuración del diálogo de biometría
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_cancel_button))
            .build()
    }

    private fun checkBiometricSupportAndAuthenticate() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("BIOMETRIC_LOGIN", "App can authenticate using biometrics.")
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("BIOMETRIC_LOGIN", "No biometric features available on this device.")
                Toast.makeText(requireContext(), getString(R.string.biometric_no_hardware), Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("BIOMETRIC_LOGIN", "Biometric features are currently unavailable.")
                Toast.makeText(requireContext(), getString(R.string.biometric_not_available), Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("BIOMETRIC_LOGIN", "The user hasn't associated any biometric credentials with their account.")
                Toast.makeText(requireContext(), getString(R.string.biometric_not_enrolled), Toast.LENGTH_LONG).show()
            }
            else -> {
                Log.e("BIOMETRIC_LOGIN", "Biometric status unknown or other error.")
                Toast.makeText(requireContext(), getString(R.string.biometric_not_available), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }
}