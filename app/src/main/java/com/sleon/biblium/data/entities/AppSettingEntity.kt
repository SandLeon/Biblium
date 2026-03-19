package com.sleon.biblium.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo
import java.io.Serializable

@Entity(
    tableName = "app_settings",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class, // los ajustes pertenecen a este usuario
            parentColumns = ["user_id"], // ficha para usuario, que mire en UserEntity
            childColumns = ["user_id"], // ficha para ajustes de dicho usuario,que mire en AppSettingEntity
            onDelete = ForeignKey.CASCADE // Si el usuario se borra, sus ajustes también
        )
    ]
)
data class AppSettingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "setting_id")
    val settingId: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val theme: Int, // 0: Claro, 1: Oscuro
    val language: String, // "es" o "en"
    @ColumnInfo(name = "is_dark_mode")
    val isDarkMode: Boolean = false,
    @ColumnInfo(name = "notifications_enabled")
    val notificationsEnabled: Boolean = true,
) : Serializable