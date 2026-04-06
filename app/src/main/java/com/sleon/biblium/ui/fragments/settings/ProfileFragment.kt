package com.sleon.biblium.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.databinding.FragmentProfileBinding
import com.sleon.biblium.ui.viewmodels.SettingViewModel
import com.sleon.biblium.ui.viewmodels.SettingViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory((requireActivity().application as BibliumApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Cargar datos del usuario actual
        loadUserData()

        // 2. Botón Atrás
        binding.ivBackProfile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 3. Botón Guardar Cambios
        binding.btnSaveProfile.setOnClickListener {
            saveChanges()
        }
    }

    private fun loadUserData() {
        val app = requireActivity().application as BibliumApplication
        lifecycleScope.launch {
            val user = app.userRepository.currentUser.first()
            user?.let {
                binding.etProfileName.setText(it.name)
                binding.etProfileEmail.setText(it.email)
            }
        }
    }

    private fun saveChanges() {
        val newName = binding.etProfileName.text.toString().trim()
        val newEmail = binding.etProfileEmail.text.toString().trim()

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(requireContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        val app = requireActivity().application as BibliumApplication
        lifecycleScope.launch {
            val user = app.userRepository.currentUser.first()
            user?.let {
                val updatedUser = it.copy(name = newName, email = newEmail)
                settingViewModel.updateProfile(updatedUser)

                Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
