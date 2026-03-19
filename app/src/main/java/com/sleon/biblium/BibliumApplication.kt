package com.sleon.biblium

import android.app.Application
import com.sleon.biblium.data.database.AppDatabase
import com.sleon.biblium.data.repository.BookRepository
import com.sleon.biblium.data.repository.SettingRepository
import com.sleon.biblium.data.repository.UserRepository

class BibliumApplication : Application() {

    // Instancia de la base de datos (Lazy para que solo se cree cuando se use)
    val database by lazy { AppDatabase.getDatabase(this) }

    // Instancias de los repositorios
    val userRepository by lazy { UserRepository(database.userDao(), database.settingDao()) }
    val bookRepository by lazy { BookRepository(database.bookDao()) }
    val settingRepository by lazy { SettingRepository(database.settingDao()) }

    override fun onCreate() {
        super.onCreate()
        // Aquí puedes inicializar librerías globales si lo necesitas más adelante
    }
}
