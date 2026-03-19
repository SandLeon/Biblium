package com.sleon.biblium.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleon.biblium.databinding.ItemBookBinding
import com.sleon.biblium.models.Book

class BookAdapter(private val bookList: List<Book>, private val onClickListener: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.binding.tvBookTitle.text = book.title
        holder.binding.tvBookAuthor.text = book.author
        holder.binding.tvBookStatus.text = book.status

        // Cambiado: Ahora cargamos un Bitmap real
        book.coverImage?.let {
            holder.binding.ivBookMain.setImageBitmap(it)
        }
        
        holder.itemView.setOnClickListener {
            onClickListener(book)
        }
    }

    override fun getItemCount(): Int = bookList.size
}
