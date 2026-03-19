package com.sleon.biblium.ui.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.R
import com.sleon.biblium.adapters.BookAdapter
import com.sleon.biblium.databinding.FragmentLibraryBinding
import com.sleon.biblium.ui.viewmodels.LibraryViewModel
import com.sleon.biblium.ui.viewmodels.LibraryViewModelFactory
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private val libraryViewModel: LibraryViewModel by viewModels {
        LibraryViewModelFactory((requireActivity().application as BibliumApplication).bookRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar RecyclerView
        binding.rvFullLibrary.layoutManager = LinearLayoutManager(requireContext())

        // 2. Observar los libros del ViewModel
        lifecycleScope.launch {
            libraryViewModel.books.collect { bookList ->
                val adapter = BookAdapter(bookList) { selectedBook ->
                    val fragmentDetail = BookDetailFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("selected_book", selectedBook)
                    fragmentDetail.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main_container, fragmentDetail)
                        .addToBackStack(null)
                        .commit()
                }
                binding.rvFullLibrary.adapter = adapter
            }
        }

        // 3. Acción del botón añadir (por ahora solo para pruebas)
        binding.fabAddBook.setOnClickListener {
            // Aquí navegarías al fragmento de "Añadir Libro"
        }

        // 4. Cargar datos (Usamos un ID de prueba 1L hasta tener el login conectado)
        libraryViewModel.fetchBooks(1L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
