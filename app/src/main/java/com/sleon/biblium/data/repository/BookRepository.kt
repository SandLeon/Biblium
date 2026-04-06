package com.sleon.biblium.data.repository

import kotlinx.coroutines.flow.Flow
import com.sleon.biblium.data.daos.BookDao
import com.sleon.biblium.data.entities.BookEntity

class BookRepository(private val bookDao: BookDao) {

    fun getBooksByUser(userId: Long): Flow<List<BookEntity>> {
        return bookDao.getBooksByUser(userId)
    }

    suspend fun getBookById(bookId: Long): BookEntity? {
        return bookDao.getBookById(bookId)
    }

    suspend fun saveBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }

    // Nuevo método para borrar por ID directamente
    suspend fun deleteBookById(bookId: Long) {
        bookDao.deleteBookById(bookId)
    }
}
