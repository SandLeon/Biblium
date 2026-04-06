package com.sleon.biblium.ui.fragments.library

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.coroutines.flow.collectLatest
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

        val app = requireActivity().application as BibliumApplication

        // Botón atrás
        binding.ivBackLibrary.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 1. Configurar RecyclerView
        binding.rvFullLibrary.layoutManager = LinearLayoutManager(requireContext())

        // 2. Observar los libros del ViewModel (USANDO viewLifecycleOwner para sincronización real)
        viewLifecycleOwner.lifecycleScope.launch {
            libraryViewModel.books.collectLatest { bookList ->
                _binding?.let { currentBinding ->
                    val query = currentBinding.etSearchLibrary.text.toString()
                    
                    if (bookList.isEmpty() && query.isNotEmpty()) {
                        currentBinding.rvFullLibrary.visibility = View.INVISIBLE
                        currentBinding.tvEmptySearchLibrary.visibility = View.VISIBLE
                    } else {
                        currentBinding.rvFullLibrary.visibility = View.VISIBLE
                        currentBinding.tvEmptySearchLibrary.visibility = View.GONE
                        
                        val adapter = BookAdapter(bookList, true) { selectedBook ->
                            val fragmentDetail = BookDetailFragment()
                            val bundle = Bundle()
                            bundle.putSerializable("selected_book", selectedBook)
                            fragmentDetail.arguments = bundle

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.main_container, fragmentDetail)
                                .addToBackStack(null)
                                .commit()
                        }
                        currentBinding.rvFullLibrary.adapter = adapter
                    }
                }
            }
        }

        // 3. Barra de búsqueda (Misma funcionalidad que Home)
        binding.etSearchLibrary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                libraryViewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. Acción del botón añadir
        binding.fabAddBook.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, AddBookFragment())
                .addToBackStack(null)
                .commit()
        }

        // 5. OBTENER EL USUARIO REAL Y CARGAR SUS LIBROS
        viewLifecycleOwner.lifecycleScope.launch {
            app.userRepository.currentUser.collect { user ->
                user?.let {
                    libraryViewModel.fetchBooks(it.userId)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
