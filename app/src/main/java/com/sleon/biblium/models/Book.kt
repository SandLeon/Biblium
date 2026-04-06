package com.sleon.biblium.models

import android.graphics.Bitmap
import java.io.Serializable

/**
 * Clase de datos para la interfaz de usuario (UI).
 * Representa la información básica de un libro para mostrar en listas o detalles.
 */
data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val status: String,
    val coverImage: Bitmap? = null,
    val review: String = "" // Añadido campo review
) : Serializable
