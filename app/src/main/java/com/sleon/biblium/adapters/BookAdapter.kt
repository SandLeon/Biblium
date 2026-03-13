package com.sleon.biblium.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleon.biblium.databinding.ItemBookBinding
import com.sleon.biblium.models.Book

class BookAdapter(private val bookList: List<Book>, private val onClickListener: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // "Contenedor": Busca las vistas de  item_book.xml
    class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    // "Constructor": Crea la vista física del libro desde el XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }
    // "Escriba": Coge los datos del libro y los pone en los TextViews
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.binding.tvBookTitle.text = book.title
        holder.binding.tvBookAuthor.text = book.author
        holder.binding.tvBookStatus.text = book.status

        // Si tienes una imagen, la ponemos. Si no, se queda el fondo gris de prueba
        book.coverImage?.let {
            holder.binding.ivBookMain.setImageResource(it)
        }
        holder.itemView.setOnClickListener {
            onClickListener(book)
        }
    }
    // 4. El "Contador": Le dice al RecyclerView cuántos libros tiene que dibujar
    override fun getItemCount(): Int = bookList.size
}
