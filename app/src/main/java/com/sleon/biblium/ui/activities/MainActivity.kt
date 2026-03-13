package com.sleon.biblium.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.sleon.biblium.R
import androidx.appcompat.app.AppCompatActivity
import com.sleon.biblium.databinding.ActivityMainBinding
import com.sleon.biblium.ui.fragments.auth.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Cargamos el Login al arrancar si el contenedor está vacío
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoginFragment())
                .commit()
        }
    }
}



