package com.sleon.biblium.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sleon.biblium.data.entities.AppSettingEntity
import com.sleon.biblium.data.entities.UserEntity
import com.sleon.biblium.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la configuración de usuario (Tema, Idioma, Perfil y Logout).
 */
class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _settings = MutableStateFlow<AppSettingEntity?>(null)
    val settings: StateFlow<AppSettingEntity?> = _settings

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    /**
     * Carga los ajustes y los datos del usuario actual.
     */
    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            userRepository.getSettings(userId).collect {
                _settings.value = it
            }
        }
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _currentUser.value = user
        }
    }

    /**
     * Guarda cambios en los ajustes (Tema, Idioma, etc).
     */
    fun saveSettings(settings: AppSettingEntity) {
        viewModelScope.launch {
            userRepository.saveSettings(settings)
        }
    }

    /**
     * Actualiza los datos del perfil (nombre, email).
     */
    fun updateProfile(user: UserEntity) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            _currentUser.value = user
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        userRepository.logoutUser()
    }
}

class SettingViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
