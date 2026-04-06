package com.sleon.biblium.data.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "books",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    val bookId: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,
    val author: String,
    val title: String,
    val status: String,
    @ColumnInfo(name = "cover_image")
    val coverImage: Bitmap? = null, 
    val summary: String,
    val review: String,
    val rating: Int,
    val category: String
) : Serializable
