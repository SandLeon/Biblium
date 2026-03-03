package com.sleon.biblium.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "app_settings",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class, // los ajustes pertenecen a este usuario
            parentColumns = ["userId"], // ficha para usuario, que mire en UserEntity
            childColumns = ["userId"], // ficha para ajustes de dicho usuario,que mire en AppSettingEntity
            onDelete = ForeignKey.CASCADE // Si el usuario se borra, sus ajustes también
        )
    ]
)
data class AppSettingEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Long, // Usamos el mismo ID del usuario para identificar sus ajustes

    val theme: Int, // 0: Claro, 1: Oscuro
    val language: String, // "es" o "en"

    @ColumnInfo(name = "is_dark_mode")
    val isDarkMode: Boolean
)