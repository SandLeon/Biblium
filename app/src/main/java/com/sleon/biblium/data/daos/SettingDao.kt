package com.sleon.biblium.data.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.sleon.biblium.data.entities.AppSettingEntity

@Dao
interface SettingDao {
    @Query("SELECT * FROM app_settings WHERE user_id = :userId")
    fun getSettingsByUserId(userId: Long): Flow<AppSettingEntity?>

    @Query("SELECT * FROM app_settings WHERE user_id = :userId")
    suspend fun getSettingsByUserIdSync(userId: Long): AppSettingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: AppSettingEntity)
}
