package com.sleon.biblium.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sleon.biblium.data.entities.BookEntity
import com.sleon.biblium.data.repository.BookRepository

/**
 * ViewModel encargado de la gestión individual de libros (Guardar, Editar, Eliminar).
 */
class BookViewModel(private val repository: BookRepository) : ViewModel() {

    /**
     * Guarda o actualiza un libro en la base de datos.
     * Se define como suspend para que la UI pueda esperar a que se complete antes de navegar hacia atrás.
     */
    suspend fun saveBook(book: BookEntity) {
        repository.saveBook(book)
    }

    /**
     * Elimina un libro de la base de datos.
     */
    suspend fun deleteBook(book: BookEntity) {
        repository.deleteBook(book)
    }

    /**
     * Elimina un libro por su ID.
     */
    suspend fun deleteBookById(bookId: Long) {
        repository.deleteBookById(bookId)
    }
}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
