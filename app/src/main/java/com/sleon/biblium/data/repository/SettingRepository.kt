package com.sleon.biblium.data.repository

import kotlinx.coroutines.flow.Flow
import com.sleon.biblium.data.daos.SettingDao
import com.sleon.biblium.data.entities.AppSettingEntity

class SettingRepository(private val settingDao: SettingDao) {

    /**
     * Obtiene los ajustes de un usuario en tiempo real usando Flow.
     * Ideal para que la UI reaccione a cambios de tema o idioma.
     */
    fun getSettings(userId: Long): Flow<AppSettingEntity?> {
        return settingDao.getSettingsByUserId(userId)
    }

    /**
     * Obtiene los ajustes de forma síncrona (suspend).
     * Útil para comprobaciones puntuales antes de cargar una pantalla.
     */
    suspend fun getSettingsSync(userId: Long): AppSettingEntity? {
        return settingDao.getSettingsByUserIdSync(userId)
    }

    /**
     * Guarda o actualiza los ajustes del usuario.
     */
    suspend fun saveSettings(settings: AppSettingEntity) {
        settingDao.insertOrUpdateSettings(settings)
    }
}
