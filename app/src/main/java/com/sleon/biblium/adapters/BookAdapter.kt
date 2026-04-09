package com.sleon.biblium.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sleon.biblium.databinding.ItemBookBinding
import com.sleon.biblium.databinding.ItemBookListBinding
import com.sleon.biblium.models.Book

class BookAdapter(
    private val bookList: List<Book>,
    private val isListView: Boolean = false,
    private val onClickListener: (Book) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Contenedor para el carrusel (diseño grande)
    class CarouselViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    // Contenedor para la lista (diseño pequeño)
    class ListViewHolder(val binding: ItemBookListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (isListView) {
            ListViewHolder(ItemBookListBinding.inflate(inflater, parent, false))
        } else {
            CarouselViewHolder(ItemBookBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val book = bookList[position]
        
        if (holder is CarouselViewHolder) {
            holder.binding.tvBookTitle.text = book.title
            holder.binding.tvBookAuthor.text = book.author
            holder.binding.tvBookStatus.text = book.status
            book.coverImage?.let { holder.binding.ivBookMain.setImageBitmap(it) }
            holder.itemView.setOnClickListener { onClickListener(book) }
        } else if (holder is ListViewHolder) {
            holder.binding.tvBookTitleList.text = book.title
            holder.binding.tvBookAuthorList.text = book.author
            holder.binding.tvBookStatusList.text = book.status
            book.coverImage?.let { holder.binding.ivBookMainList.setImageBitmap(it) }
            holder.itemView.setOnClickListener { onClickListener(book) }
        }
    }

    override fun getItemCount(): Int = bookList.size
}
