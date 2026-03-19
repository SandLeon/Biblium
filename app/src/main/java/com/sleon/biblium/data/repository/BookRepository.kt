package com.sleon.biblium.data.repository

import kotlinx.coroutines.flow.Flow
import com.sleon.biblium.data.daos.BookDao
import com.sleon.biblium.data.entities.BookEntity

class BookRepository(private val bookDao: BookDao) {

    /**
     * Obtiene todos los libros de un usuario específico.
     * Devuelve Flow para que la UI se actualice automáticamente en tiempo real.
     */
    fun getBooksByUser(userId: Long): Flow<List<BookEntity>> {
        return bookDao.getBooksByUser(userId)
    }

    /**
     * Busca un libro por su ID.
     */
    suspend fun getBookById(bookId: Long): BookEntity? {
        return bookDao.getBookById(bookId)
    }

    /**
     * Guarda un libro (inserta o actualiza si ya existe).
     */
    suspend fun saveBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    /**
     * Actualiza la información de un libro existente.
     */
    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
    }

    /**
     * Elimina un libro de la base de datos.
     */
    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}
