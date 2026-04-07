package com.sleon.biblium.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.sleon.biblium.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.databinding.ActivityMainBinding
import com.sleon.biblium.ui.fragments.auth.LoginFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Cargamos el Login al arrancar si el contenedor está vacío
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoginFragment())
                .commit()
        }
        observeThemePreference()
    }

    private fun observeThemePreference() {
        val app = application as BibliumApplication

        // Usamos lifecycleScope para que esta corrutina viva mientras la App esté abierta
        lifecycleScope.launch {
            //usuario logueado?
            app.userRepository.currentUser.collect { user ->
                if (user == null) {
                    // Si no hay nadie (Logout), volvemos al modo claro por defecto
                    applyTheme(false)
                } else {
                    //  Si hay usuario, escuchamos sus ajustes de la base de datos
                    app.settingRepository.getSettings(user.userId).collect { settings ->
                        settings?.let { s ->
                            applyTheme(s.isDarkMode)
                        }
                    }
                }
            }
        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        val targetMode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        if (AppCompatDelegate.getDefaultNightMode() != targetMode) {
            AppCompatDelegate.setDefaultNightMode(targetMode)
        }
    }
}



