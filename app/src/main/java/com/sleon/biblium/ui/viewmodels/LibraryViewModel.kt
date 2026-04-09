package com.sleon.biblium.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.sleon.biblium.data.repository.BookRepository
import com.sleon.biblium.models.Book
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: BookRepository) : ViewModel() {

    private val _allBooks = MutableStateFlow<List<Book>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _statusFilter = MutableStateFlow("Todos") // "Todos", "Leído", "Pendiente", "Leyendo"
    
    private val _filteredBooks = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _filteredBooks

    private var fetchJob: Job? = null

    /**
     * Carga los libros del usuario. Cancela cualquier carga previa para evitar duplicados o pérdida de datos.
     */
    fun fetchBooks(userId: Long) {
        fetchJob?.cancel() 
        fetchJob = viewModelScope.launch {
            repository.getBooksByUser(userId).collect { entities ->
                val domainBooks = entities.map { entity ->
                    Book(
                        id = entity.bookId,
                        title = entity.title,
                        author = entity.author,
                        status = entity.status,
                        coverImage = entity.coverImage,
                        review = entity.review
                    )
                }
                _allBooks.value = domainBooks
                applyFilter() 
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    fun setStatusFilter(status: String) {
        _statusFilter.value = status
        applyFilter()
    }

    private fun applyFilter() {
        val query = _searchQuery.value.trim().lowercase()
        val status = _statusFilter.value

        var filtered = _allBooks.value

        // Filtrar por estado si no es "Todos"
        if (status != "Todos") {
            filtered = filtered.filter { it.status == status }
        }

        // Filtrar por búsqueda
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.title.lowercase().contains(query) || it.author.lowercase().contains(query)
            }
        }

        _filteredBooks.value = filtered
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
