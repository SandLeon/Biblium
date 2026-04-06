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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sleon.biblium.BibliumApplication
import com.sleon.biblium.data.entities.BookEntity
import com.sleon.biblium.databinding.FragmentAddBookBinding
import com.sleon.biblium.ui.viewmodels.BookViewModel
import com.sleon.biblium.ui.viewmodels.BookViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddBookFragment : Fragment() {

    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!
    
    private var selectedImageBitmap: Bitmap? = null

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((requireActivity().application as BibliumApplication).bookRepository)
    }

    // Selector de imágenes de la galería
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                // Convertir la URI de la imagen en un Bitmap
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                }
                
                selectedImageBitmap = bitmap
                binding.ivAddPhoto.setImageBitmap(bitmap) // Mostrar la imagen en el recuadro
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        
        // Al hacer clic en el recuadro gris, abrimos la galería
        binding.ivAddPhoto.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnCancelNewBook.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSaveNewBook.setOnClickListener {
            saveBook()
        }
    }

    private fun setupSpinner() {
        val statuses = arrayOf("Pendiente", "Leyendo", "Leído")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStatus.adapter = adapter
    }

    private fun saveBook() {
        val app = requireActivity().application as BibliumApplication
        val title = binding.etAddBookTitle.text.toString()
        val author = binding.etAddAuthor.text.toString()
        val status = binding.spnStatus.selectedItem.toString()
        val review = binding.etAddReview.text.toString()

        if (title.isBlank() || author.isBlank()) {
            Toast.makeText(requireContext(), "El título y el autor son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            // Usamos first() para obtener el usuario actual una sola vez
            val user = app.userRepository.currentUser.first()
            user?.let {
                val newBook = BookEntity(
                    userId = it.userId,
                    title = title,
                    author = author,
                    status = status,
                    summary = "",
                    review = review,
                    coverImage = selectedImageBitmap,
                    rating = 0,
                    category = "General"
                )
                // Ahora saveBook es suspend y esperamos a que termine antes de salir
                bookViewModel.saveBook(newBook)
                Toast.makeText(requireContext(), "Libro guardado con éxito", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
