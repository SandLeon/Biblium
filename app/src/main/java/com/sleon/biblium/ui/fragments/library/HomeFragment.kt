package com.sleon.biblium.ui.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sleon.biblium.R
import com.sleon.biblium.adapters.BookAdapter
import com.sleon.biblium.databinding.FragmentHomeBinding
import com.sleon.biblium.models.Book
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Creamos unos libros de prueba (Inventados por ahora)
        val testBooks = listOf(
            Book("Rey entre Sombras", "Melissa Landers", "Leído",R.drawable.rey),
            Book("Arbol de sangre", "Nidia Heras", "Pendiente",R.drawable.arbol),
            Book("Captive", "Sarah Rivens", "Leyendo",R.drawable.captive)
        )

        // 2. Preparamos el Adapter con libros de prueba
        val bookAdapter = BookAdapter(testBooks) { book ->
            println("DEBUG_BIBLIUM: He pulsado el libro ${book.title}")
            val fragmentDetail = BookDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("selected_book", book) // Guardamos el libro
            fragmentDetail.arguments = bundle

            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true) // Optimización nativa de animaciones
                .replace(R.id.main_container,fragmentDetail)
                .addToBackStack(null) // Esto permite que el botón "Atrás" del móvil funcione
                .commit()
        }

        binding.rvLibrary.adapter = bookAdapter

        val snapHelper = PagerSnapHelper()//carrusel
        snapHelper.attachToRecyclerView(binding.rvLibrary)

        //Efecto Zoom
        binding.rvLibrary.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerEffect()
            }
        })
        
        binding.rvLibrary.post {
            recyclerEffect()
        }
    }

    private fun recyclerEffect() {
        val recyclerView = binding.rvLibrary
        val centerX = recyclerView.width / 2f // El ancho total entre 2

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childCenterX = (child.left + child.right) / 2f
            val distanceFromCenter = abs(centerX - childCenterX)
            val scaleFactor = 1f - (distanceFromCenter / recyclerView.width) * 0.3f
            child.scaleX = scaleFactor
            child.scaleY = scaleFactor
            child.alpha = scaleFactor.coerceAtLeast(0.5f)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
