package com.sleon.biblium.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sleon.biblium.data.entities.UserEntity
import com.sleon.biblium.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado exclusivamente de la Autenticación (Login y Registro).
 */
class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Estado del usuario actual
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    // Estado del proceso de login (null: inicial, true: éxito, false: error)
    private val _authStatus = MutableStateFlow<Boolean?>(null)
    val authStatus: StateFlow<Boolean?> = _authStatus

    /**
     * Intenta iniciar sesión.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.loginUser(email, password)
            if (user != null) {
                _currentUser.value = user
                _authStatus.value = true
            } else {
                _authStatus.value = false
            }
        }
    }

    /**
     * Registra un nuevo usuario y loguea automáticamente.
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val id = userRepository.registerUser(name, email, password)
                if (id != -1L) {
                    login(email, password)
                }
            } catch (e: Exception) {
                _authStatus.value = false
            }
        }
    }

    fun resetStatus() {
        _authStatus.value = null
    }
}

/**
 * Fábrica para poder pasarle el repositorio al ViewModel.
 */
class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
