package com.sleon.biblium.data.entities

// Para que funcione Room
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true), // No permite dos usuarios con el mismo email
        Index(value = ["name"], unique = true)   // No permite dos usuarios con el mismo nombre
    ]
)

//data class (Clase solo para guardar datos)
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    //val  significa constante
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val salt: String
)