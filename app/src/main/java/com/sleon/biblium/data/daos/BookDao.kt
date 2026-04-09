package com.sleon.biblium.data.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.sleon.biblium.data.entities.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM books WHERE user_id = :userId")
    fun getBooksByUser(userId: Long): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    // Borrar por ID
    @Query("DELETE FROM books WHERE book_id = :bookId")
    suspend fun deleteBookById(bookId: Long)

    @Query("SELECT * FROM books WHERE book_id = :bookId")
    suspend fun getBookById(bookId: Long): BookEntity?
}
