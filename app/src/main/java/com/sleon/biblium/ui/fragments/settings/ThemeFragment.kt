package com.sleon.biblium.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.databinding.FragmentThemeBinding
import com.sleon.biblium.ui.viewmodels.SettingViewModel
import com.sleon.biblium.ui.viewmodels.SettingViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThemeFragment : Fragment() {

    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory((requireActivity().application as BibliumApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Cargar el tema actual del usuario
        loadCurrentTheme()

        binding.ivBackTheme.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSaveTheme.setOnClickListener {
            applyAndSaveTheme()
        }
    }

    private fun loadCurrentTheme() {
        val app = requireActivity().application as BibliumApplication
        lifecycleScope.launch {
            val user = app.userRepository.currentUser.first()
            user?.let {
                val settings = app.userRepository.getSettingsSync(it.userId)
                settings?.let { s ->
                    if (s.isDarkMode) {
                        binding.rbDarkTheme.isChecked = true
                    } else {
                        binding.rbLightTheme.isChecked = true
                    }
                }
            }
        }
    }

    private fun applyAndSaveTheme() {
        val isDarkMode = binding.rbDarkTheme.isChecked
        val app = requireActivity().application as BibliumApplication

        lifecycleScope.launch {
            val user = app.userRepository.currentUser.first()
            user?.let { u ->
                val currentSettings = app.userRepository.getSettingsSync(u.userId)
                currentSettings?.let { s ->
                    val updatedSettings = s.copy(isDarkMode = isDarkMode, theme = if (isDarkMode) 1 else 0)
                    settingViewModel.saveSettings(updatedSettings)

                    // Aplicar el cambio visualmente en toda la app
                    if (isDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                    Toast.makeText(requireContext(), "Tema aplicado", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
