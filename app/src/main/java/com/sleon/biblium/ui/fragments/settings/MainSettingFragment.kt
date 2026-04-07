package com.sleon.biblium.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.R
import com.sleon.biblium.databinding.FragmentMainSettingBinding
import com.sleon.biblium.ui.fragments.auth.LoginFragment
import com.sleon.biblium.ui.viewmodels.SettingViewModel
import com.sleon.biblium.ui.viewmodels.SettingViewModelFactory

class MainSettingFragment : Fragment() {

    private var _binding: FragmentMainSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels {
        val app = requireActivity().application as BibliumApplication
        SettingViewModelFactory(app.userRepository, app.settingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón Atrás
        binding.ivBackSettings.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Opción Perfil
        binding.tvOptionProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // Opción Tema
        binding.tvOptionTheme.setOnClickListener {
             parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ThemeFragment())
                .addToBackStack(null)
                .commit()
        }

        // Opción Idioma
        binding.tvOptionLanguage.setOnClickListener {
             parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LanguageFragment())
                .addToBackStack(null)
                .commit()
        }

        // Opción Cerrar Sesión
        binding.tvLogout.setOnClickListener {
            settingViewModel.logout()
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
            
            // Navegar al Login y limpiar el historial de fragmentos
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoginFragment())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
