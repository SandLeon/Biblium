package com.sleon.biblium.data.daos
import androidx.room.*
import com.sleon.biblium.data.entities.AppSettingEntity


@Dao
interface AppSettingDao {
    @Query("SELECT * FROM app_settings WHERE user_id = :userId")
    suspend fun getSettingsByUserId(userId: Long): AppSettingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: AppSettingEntity)
}