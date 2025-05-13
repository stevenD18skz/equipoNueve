package com.example.dogapp.login // Reemplaza con tu package name

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

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        executor = ContextCompat.getMainExecutor(requireContext())

        setupBiometricAuth()

        binding.lottieAnimationViewFingerprint.setOnClickListener {
            // Criterio 5: Mostrar ventana emergente de biometría
            checkBiometricSupportAndAuthenticate()
        }

        loginViewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate == true) {
                Toast.makeText(requireContext(), getString(R.string.navigate_to_home_admin_citas), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                loginViewModel.onNavigationComplete() // Resetear el evento
            }
        }
    }

    private fun setupBiometricAuth() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
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
            .setNegativeButtonText(getString(R.string.biometric_cancel_button)) // Botón cancelar
            // También puedes usar .setConfirmationRequired(false)
            // O .setDeviceCredentialAllowed(true) si quieres permitir PIN/Patrón/Contraseña como fallback
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
                // Opcional: Ofrecer al usuario ir a los ajustes para enrolar una huella
                // val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                //     putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                // }
                // startActivity(enrollIntent)
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