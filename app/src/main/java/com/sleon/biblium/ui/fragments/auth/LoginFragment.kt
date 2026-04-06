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
import com.sleon.biblium.databinding.FragmentLoginBinding
import com.sleon.biblium.ui.fragments.library.HomeFragment
import com.sleon.biblium.ui.viewmodels.AuthViewModel
import com.sleon.biblium.ui.viewmodels.AuthViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((requireActivity().application as BibliumApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoginStatus()

        binding.btnEnter.setOnClickListener {

            val email = binding.etUser.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegisterRedirect.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeLoginStatus() {
        lifecycleScope.launch {
            authViewModel.authStatus.collect { success ->
                when (success) {
                    true -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main_container, HomeFragment())
                            .commit()
                    }
                    false -> {
                        Toast.makeText(requireContext(), "Error: Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        authViewModel.resetStatus()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
