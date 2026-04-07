package com.sleon.biblium.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sleon.biblium.data.entities.AppSettingEntity
import com.sleon.biblium.data.entities.UserEntity
import com.sleon.biblium.data.repository.SettingRepository
import com.sleon.biblium.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


 //ViewModel encargado de la configuración de usuario (Tema, Idioma, Perfil y Logout).

class SettingViewModel(
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository
): ViewModel() {

    private val _settings = MutableStateFlow<AppSettingEntity?>(null)
    val settings: StateFlow<AppSettingEntity?> = _settings

    val currentUser: StateFlow<UserEntity?> = userRepository.currentUser

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess


    //Carga los ajustes y los datos del usuario actual.
    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            settingRepository.getSettings(userId).collect {
                _settings.value = it
            }
        }
    }


      //Guarda cambios en los ajustes (Tema, Idioma, etc).
    fun saveSettings(settings: AppSettingEntity) {
        viewModelScope.launch {
            settingRepository.saveSettings(settings)
        }
    }


     //Actualiza los datos del perfil (nombre, email).
    fun updateProfile(user: UserEntity) {
        viewModelScope.launch {
            _updateSuccess.value = false
            val isSuccess = userRepository.updateUser(user)
            _updateSuccess.value = isSuccess
        }
    }

   //Cerrar sesion
    fun logout() {
        userRepository.logoutUser()
    }
}

class SettingViewModelFactory(private val repository: UserRepository,private val settingRepo: SettingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel( repository, settingRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
