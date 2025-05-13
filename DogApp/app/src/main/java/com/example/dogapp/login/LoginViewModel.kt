package com.example.dogapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    fun onAuthenticationSuccess() {
        _navigateToHome.value = true
    }

    fun onNavigationComplete() {
        _navigateToHome.value = null
    }
}