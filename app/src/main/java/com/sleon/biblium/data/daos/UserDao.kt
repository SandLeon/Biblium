package com.sleon.biblium.data.daos

import androidx.room.*
import com.sleon.biblium.data.entities.UserEntity

//Ordenes para la base de datos
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT) // si registro con mismo email, se aborta operacion
    suspend fun registerUser(user: UserEntity): Long //suspend: la funcion va a ser pesada, no se congelara la pantalla

    @Query("SELECT * FROM users WHERE user_id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    // En el Login para comprobar si el email existe
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
