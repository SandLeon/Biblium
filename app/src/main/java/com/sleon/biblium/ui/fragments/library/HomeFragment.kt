package com.sleon.biblium.ui.fragments.library

import android.graphics.BitmapFactory
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

        // CORRECCIÓN: Convertimos los recursos Drawable a Bitmap para que coincidan con el modelo Book
        val testBooks = listOf(
            Book("Rey entre Sombras", "Melissa Landers", "Leído", BitmapFactory.decodeResource(resources, R.drawable.rey)),
            Book("Arbol de sangre", "Nidia Heras", "Pendiente", BitmapFactory.decodeResource(resources, R.drawable.arbol)),
            Book("Captive", "Sarah Rivens", "Leyendo", BitmapFactory.decodeResource(resources, R.drawable.captive))
        )

        val bookAdapter = BookAdapter(testBooks) { book ->
            val fragmentDetail = BookDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("selected_book", book)
            fragmentDetail.arguments = bundle

            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, fragmentDetail)
                .addToBackStack(null)
                .commit()
        }

        binding.rvLibrary.adapter = bookAdapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvLibrary)

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
        val centerX = recyclerView.width / 2f

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
