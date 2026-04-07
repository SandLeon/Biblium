package com.sleon.biblium.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.sleon.biblium.data.daos.UserDao
import com.sleon.biblium.data.daos.SettingDao
import com.sleon.biblium.data.entities.UserEntity
import com.sleon.biblium.data.entities.AppSettingEntity
import com.sleon.biblium.utils.SecurityUtils

class UserRepository(
    private val userDao: UserDao,
    //private val settingDao: SettingDao
) {

    // Estado global del usuario logueado en la sesión actual
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    // --- Operaciones de Usuario ---

    suspend fun registerUser(name: String, email: String, password: String): Long {
        val salt = SecurityUtils.generateSalt()
        val passwordHash = SecurityUtils.hashPassword(password, salt)
        val user = UserEntity(
            name = name,
            email = email,
            passwordHash = passwordHash,
            salt = salt
        )
        val userId = userDao.registerUser(user)
        _currentUser.value = user.copy(userId = userId)
        return userId
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && SecurityUtils.verifyPassword(password, user.salt, user.passwordHash)) {
            _currentUser.value = user // Guardamos al usuario en la sesión actual
            user
        } else {
            null
        }
    }

    suspend fun updateUser(user: UserEntity): Boolean {
        return try {
            userDao.updateUser(user)
            if (_currentUser.value?.userId == user.userId) {
                _currentUser.value = user
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun logoutUser() {
        _currentUser.value = null
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(id: Long): UserEntity? {
        return userDao.getUserById(id)
    }
}
