package com.sleon.biblium.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sleon.biblium.data.daos.BookDao
import com.sleon.biblium.data.daos.UserDao
import com.sleon.biblium.data.entities.AppSettingEntity
import com.sleon.biblium.data.entities.BookEntity
import com.sleon.biblium.data.entities.UserEntity


//Meter las 3 tablas en la base de datos
@Database(
    entities = [UserEntity::class, BookEntity::class, AppSettingEntity::class],
    version = 1, // cambiar a 2 si añadimos columna nueva
    exportSchema = false
)
//plano para Room
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao

    //SINGLETON, UNA SOLA Instancia en BD con "companion object"
    companion object {
        @Volatile //Los cambios son visibles a otros hilos.
        private var INSTANCE: AppDatabase? = null //Guardamos BD

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) { //Evita el choque de trenes.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biblium_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
