package com.sleon.biblium.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.R
import com.sleon.biblium.databinding.FragmentRegisterBinding
import com.sleon.biblium.ui.fragments.library.HomeFragment
import com.sleon.biblium.ui.viewmodels.AuthViewModel
import com.sleon.biblium.ui.viewmodels.AuthViewModelFactory
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((requireActivity().application as BibliumApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar el estado de la autenticación
        observeAuthStatus()

        binding.btnRegister.setOnClickListener {
            val name = binding.etrUserName.text.toString().trim()
            val email = binding.etrEmail.text.toString().trim()
            val password = binding.etrPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.register(name, email, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeAuthStatus() {
        lifecycleScope.launch {
            authViewModel.authStatus.collect { success ->
                when (success) {
                    true -> {
                        Toast.makeText(requireContext(), "¡Registro completado!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main_container, HomeFragment())
                            .commit()
                    }
                    false -> {
                        Toast.makeText(requireContext(), "Error al registrar. El email ya podría estar en uso.", Toast.LENGTH_LONG).show()
                        authViewModel.resetStatus()
                    }
                    else -> {} // Estado inicial (null)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
