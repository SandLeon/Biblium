package com.sleon.biblium.utils

import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

object SecurityUtils {

    // Generar una sal aleatoria
    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    // Hash de contraseña con sal usando SHA-256
    fun hashPassword(password: String, salt: String): String {
        val saltedPassword = salt + password
        val md = MessageDigest.getInstance("SHA-256")
        val hashedBytes = md.digest(saltedPassword.toByteArray())
        return Base64.encodeToString(hashedBytes, Base64.NO_WRAP)
    }

    // Verificar si la contraseña introducida coincide con el hash guardado
    fun verifyPassword(password: String, salt: String, storedHash: String): Boolean {
        val newHash = hashPassword(password, salt)
        return newHash == storedHash
    }
}
