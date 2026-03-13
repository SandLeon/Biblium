package com.sleon.biblium.models
import java.io.Serializable
// Heredar de Serializable permite que el objeto se convierta en datos binarios
data class Book (
    val title: String,
    val author: String,
    val status: String,
    val coverImage: Int? = null
): Serializable
