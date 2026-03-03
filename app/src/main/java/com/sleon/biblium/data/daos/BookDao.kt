package com.sleon.biblium.data.daos

import androidx.room.*
import com.sleon.biblium.data.entities.BookEntity
import kotlinx.coroutines.flow.Flow



@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    // Obtener todos los libros de un usuario específico ordenados por título
    @Query("SELECT * FROM books WHERE user_id = :userId ORDER BY title ASC")
    fun getBooksByUser(userId: Long): Flow<List<BookEntity>>

    // Buscar libros por título o autor (útil para tu buscador)
    @Query("SELECT * FROM books WHERE user_id = :userId AND (title LIKE '%' || :search || '%' OR author LIKE '%' || :search || '%')")
    fun searchBooks(userId: Long, search: String): Flow<List<BookEntity>>

    // Obtener un libro por su ID para ver los detalles
    @Query("SELECT * FROM books WHERE bookId = :id")
    suspend fun getBookById(id: Long): BookEntity?
}