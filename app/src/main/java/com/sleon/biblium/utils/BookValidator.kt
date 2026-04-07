package com.sleon.biblium.utils

object BookValidator {
    fun isBookValid(title: String, author: String): Boolean {
        // Un libro es válido si el título no está vacío y el autor tiene al menos 3 letras
        return title.isNotBlank() && author.trim().length >= 3
    }
}