package com.sleon.biblium.ui.fragments.library

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.R
import com.sleon.biblium.data.entities.BookEntity
import com.sleon.biblium.databinding.FragmentBookDetailBinding
import com.sleon.biblium.models.Book
import com.sleon.biblium.ui.viewmodels.BookViewModel
import com.sleon.biblium.ui.viewmodels.BookViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookDetailFragment : Fragment() {
    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    private var selectedImageBitmap: Bitmap? = null

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((requireActivity().application as BibliumApplication).bookRepository)
    }

    private val imagePickerLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                }
                selectedImageBitmap = bitmap
                binding.ivPhotoLibrary.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as BibliumApplication
        val statuses = arrayOf("Pendiente", "Leyendo", "Leído")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStatusDetail.adapter = adapter

        val book = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("selected_book", Book::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("selected_book") as? Book
        }

        book?.let { currentBook ->
            binding.tvTitle.text = currentBook.title
            binding.tvAuthor.text = currentBook.author
            binding.etReview.setText(currentBook.review)
            
            val statusPosition = statuses.indexOf(currentBook.status)
            if (statusPosition >= 0) {
                binding.spnStatusDetail.setSelection(statusPosition)
            }
            
            currentBook.coverImage?.let { bitmap ->
                binding.ivPhotoLibrary.setImageBitmap(bitmap)
                selectedImageBitmap = bitmap
            }

            binding.ivPhotoLibrary.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }

            binding.btnSummary.setOnClickListener {
                val fragmentResume = ResumeFragment()
                val bundle = Bundle()
                bundle.putSerializable("selected_book", currentBook)
                fragmentResume.arguments = bundle
                
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragmentResume)
                    .addToBackStack(null)
                    .commit()
            }

            binding.btnSaveDetail.setOnClickListener {
                val updatedStatus = binding.spnStatusDetail.selectedItem.toString()
                val updatedReview = binding.etReview.text.toString()

                lifecycleScope.launch {
                    try {
                        val user = app.userRepository.currentUser.first()
                        val existingBook = app.bookRepository.getBookById(currentBook.id)
                        
                        user?.let {
                            val bookToUpdate = BookEntity(
                                bookId = currentBook.id,
                                userId = it.userId,
                                title = currentBook.title,
                                author = currentBook.author,
                                status = updatedStatus,
                                summary = existingBook?.summary ?: "",
                                review = updatedReview,
                                coverImage = selectedImageBitmap,
                                rating = 0,
                                category = "General"
                            )
                            bookViewModel.saveBook(bookToUpdate)
                            context?.let { ctx ->
                                Toast.makeText(ctx, "Libro actualizado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        context?.let { ctx ->
                            Toast.makeText(ctx, "Error al guardar el libro", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            binding.btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        bookViewModel.deleteBookById(currentBook.id)
                        Toast.makeText(requireContext(), "Libro eliminado", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
