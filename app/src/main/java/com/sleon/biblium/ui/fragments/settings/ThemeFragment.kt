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
        val app = requireActivity().application as BibliumApplication
        SettingViewModelFactory(app.userRepository, app.settingRepository)
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

        observeSettings()
        binding.ivBackTheme.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSaveTheme.setOnClickListener {
            applyAndSaveTheme()
        }
    }


    private fun observeSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            settingViewModel.settings.collect { settings ->
                settings?.let {
                    // Actualizamos la UI según el estado que emita el Flow
                    binding.rbDarkTheme.isChecked = it.isDarkMode
                    binding.rbLightTheme.isChecked = !it.isDarkMode
                }
            }
        }

        //  ViewModel  empieca a cargar los datos
        val userId = settingViewModel.currentUser.value?.userId
        userId?.let { settingViewModel.loadUserData(it) }
    }

    private fun applyAndSaveTheme() {
        val isDarkMode = binding.rbDarkTheme.isChecked
        val app = requireActivity().application as BibliumApplication

        lifecycleScope.launch {
            val user = app.userRepository.currentUser.first()
            user?.let { u ->
                val currentSettings = settingViewModel.settings.value

                currentSettings?.let { s ->
                    val updatedSettings = s.copy(isDarkMode = isDarkMode, theme = if (isDarkMode) 1 else 0)
                    settingViewModel.saveSettings(updatedSettings)

                    // Aplicar el cambio visualmente en toda la app
                    val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(mode)

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
