package com.sleon.biblium.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sleon.biblium.data.daos.BookDao
import com.sleon.biblium.data.daos.SettingDao
import com.sleon.biblium.data.daos.UserDao
import com.sleon.biblium.data.entities.AppSettingEntity
import com.sleon.biblium.data.entities.BookEntity
import com.sleon.biblium.data.entities.UserEntity

@Database(
    entities = [UserEntity::class, BookEntity::class, AppSettingEntity::class],
    version = 3, // Incrementado a 3 para forzar la recreación tras el cambio de esquema
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun settingDao(): SettingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biblium_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
