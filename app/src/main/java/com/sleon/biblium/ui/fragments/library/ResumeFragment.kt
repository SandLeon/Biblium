package com.sleon.biblium.ui.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.databinding.ResumeBinding
import com.sleon.biblium.models.Book
import com.sleon.biblium.ui.viewmodels.BookViewModel
import com.sleon.biblium.ui.viewmodels.BookViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ResumeFragment : Fragment() {

    private var _binding: ResumeBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((requireActivity().application as BibliumApplication).bookRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as BibliumApplication

        @Suppress("DEPRECATION")
        val book = arguments?.getSerializable("selected_book") as? Book

        book?.let { currentBook ->
            // Cargar el resumen actual
            lifecycleScope.launch {
                try {
                    val fullBook = app.bookRepository.getBookById(currentBook.id)
                    if (_binding != null) {
                        binding.etResume.setText(fullBook?.summary ?: "")
                    }
                } catch (e: Exception) {
                    // Error silencioso al cargar
                }
            }

            binding.btnSaveResume.setOnClickListener {
                val updatedSummary = binding.etResume.text.toString()
                lifecycleScope.launch {
                    try {
                        val user = app.userRepository.currentUser.first()
                        val fullBook = app.bookRepository.getBookById(currentBook.id)
                        
                        if (user != null && fullBook != null) {
                            val updatedBook = fullBook.copy(summary = updatedSummary)
                            bookViewModel.saveBook(updatedBook)
                            
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Resumen guardado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        if (isAdded) {
                            Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            binding.btnDeleteResume.setOnClickListener {
                binding.etResume.setText("")
            }
        }

        binding.ivBackProfileResume.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
