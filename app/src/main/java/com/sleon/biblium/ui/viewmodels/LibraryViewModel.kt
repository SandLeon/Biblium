package com.sleon.biblium.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.sleon.biblium.data.entities.BookEntity
import com.sleon.biblium.data.repository.BookRepository
import com.sleon.biblium.models.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: BookRepository) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    /**
     * Carga los libros de la base de datos y los transforma al modelo de la UI.
     */
    fun fetchBooks(userId: Long) {
        viewModelScope.launch {
            repository.getBooksByUser(userId).collect { entities ->
                val domainBooks = entities.map { entity ->
                    Book(
                        title = entity.title,
                        author = entity.author,
                        status = entity.status,
                        coverImage = entity.coverImage as? Bitmap // Asumiendo que el converter ya lo maneja
                    )
                }
                _books.value = domainBooks
            }
        }
    }
}

class LibraryViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
