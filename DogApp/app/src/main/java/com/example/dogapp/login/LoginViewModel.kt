package com.example.dogapp.login

// Importaciones necesarias para ViewModel y LiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel para la pantalla de login.
 * Gestiona la navegación a la pantalla principal tras un login exitoso.
 */
class LoginViewModel : ViewModel() {
    // LiveData privada que controla la navegación a Home
    private val _navigateToHome = MutableLiveData<Boolean?>()

    // LiveData pública para ser observada por la UI
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    /**
     * Llamar cuando la autenticación fue exitosa para navegar a Home
     */
    fun onAuthenticationSuccess() {
        _navigateToHome.value = true
    }

    /**
     * Llamar cuando la navegación ya se completó para resetear el estado
     */
    fun onNavigationComplete() {
        _navigateToHome.value = null
    }
}