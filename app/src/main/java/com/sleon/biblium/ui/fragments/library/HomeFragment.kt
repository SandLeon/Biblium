package com.sleon.biblium.ui.fragments.library

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.R
import com.sleon.biblium.adapters.BookAdapter
import com.sleon.biblium.databinding.FragmentHomeBinding
import com.sleon.biblium.ui.fragments.settings.MainSettingFragment
import com.sleon.biblium.ui.viewmodels.LibraryViewModel
import com.sleon.biblium.ui.viewmodels.LibraryViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val libraryViewModel: LibraryViewModel by viewModels {
        LibraryViewModelFactory((requireActivity().application as BibliumApplication).bookRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as BibliumApplication

        // 1. Configurar RecyclerView
        binding.rvLibrary.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        if (binding.rvLibrary.onFlingListener == null) {
            PagerSnapHelper().attachToRecyclerView(binding.rvLibrary)
        }

        // 2. Lógica del botón Filtrar
        binding.btnFilter.setOnClickListener {
            showFilterMenu(it)
        }

        // 3. Observar libros filtrados (USANDO viewLifecycleOwner para evitar cierres)
        viewLifecycleOwner.lifecycleScope.launch {
            libraryViewModel.books.collectLatest { bookList ->
                _binding?.let { currentBinding ->
                    val query = currentBinding.etSearchHome.text.toString()
                    
                    if (bookList.isEmpty() && query.isNotEmpty()) {
                        currentBinding.rvLibrary.visibility = View.INVISIBLE
                        currentBinding.tvEmptySearch.visibility = View.VISIBLE
                    } else {
                        currentBinding.rvLibrary.visibility = View.VISIBLE
                        currentBinding.tvEmptySearch.visibility = View.GONE
                        
                        currentBinding.rvLibrary.adapter = BookAdapter(bookList) { book ->
                            val fragmentDetail = BookDetailFragment()
                            val bundle = Bundle()
                            bundle.putSerializable("selected_book", book)
                            fragmentDetail.arguments = bundle
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.main_container, fragmentDetail)
                                .addToBackStack(null)
                                .commit()
                        }
                        currentBinding.rvLibrary.post { recyclerEffect() }
                    }
                }
            }
        }

        // 4. Barra de búsqueda
        binding.etSearchHome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                libraryViewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 5. Usuario y Carga inicial
        viewLifecycleOwner.lifecycleScope.launch {
            app.userRepository.currentUser.collect { user ->
                _binding?.let { currentBinding ->
                    user?.let {
                        currentBinding.tvUserName.text = it.name
                        libraryViewModel.fetchBooks(it.userId)
                    }
                }
            }
        }

        // Navegación
        binding.btnToggleList.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LibraryFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.fabAddBookHome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, AddBookFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.ibSettingHome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainSettingFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.rvLibrary.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerEffect()
            }
        })
    }

    private fun showFilterMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Todos")
        popup.menu.add("Leído")
        popup.menu.add("Leyendo")
        popup.menu.add("Pendiente")

        popup.setOnMenuItemClickListener { item ->
            libraryViewModel.setStatusFilter(item.title.toString())
            true
        }
        popup.show()
    }

    private fun recyclerEffect() {
        _binding?.let { currentBinding ->
            val recyclerView = currentBinding.rvLibrary
            if (recyclerView.childCount == 0 || recyclerView.width <= 0) return
            val centerX = recyclerView.width / 2f
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i) ?: continue
                val childCenterX = (child.left + child.right) / 2f
                val distanceFromCenter = abs(centerX - childCenterX)
                val scaleFactor = 1f - (distanceFromCenter / recyclerView.width.coerceAtLeast(1)) * 0.3f
                child.scaleX = scaleFactor.coerceIn(0.7f, 1f)
                child.scaleY = scaleFactor.coerceIn(0.7f, 1f)
                child.alpha = scaleFactor.coerceAtLeast(0.5f)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
