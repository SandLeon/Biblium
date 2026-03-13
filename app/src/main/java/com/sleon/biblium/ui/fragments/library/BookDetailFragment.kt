package com.sleon.biblium.ui.fragments.library

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleon.biblium.R
import com.sleon.biblium.databinding.FragmentBookDetailBinding
import com.sleon.biblium.models.Book


class BookDetailFragment : Fragment() {
    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val book = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("selected_book", Book::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("selected_book") as? Book
        }

        // 2. Si el libro ha llegado bien, rellenamos los campos
        book?.let {
            binding.tvTitle.setText(it.title)
            binding.tvAuthor.setText(it.author)

            it.coverImage?.let { imagen ->
            binding.ivPhotoLibrary.setImageResource(it.coverImage!!)}
        }

        binding.ivBackArrow.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}