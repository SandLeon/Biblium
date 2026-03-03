package com.sleon.biblium.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "books", // <--- ESTO quita el rojo de "books" en el DAO
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
    val bookId: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    val title: String,
    val author: String,
    val description: String,
    val status: String
)