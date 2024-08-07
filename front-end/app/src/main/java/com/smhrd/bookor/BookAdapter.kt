package com.smhrd.bookor

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.smhrd.bookor.Book
import com.smhrd.bookor.databinding.ItemBookBinding

class BookAdapter(
    private val bookList: List<Book>,
    private val itemClickListener: (Book) -> Unit // 클릭 리스너를 인자로 받음
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {

    private var filteredBookList: List<Book> = bookList

    inner class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.bookTitle.text = book.title
            binding.bookProgress.text = "진행률: ${book.progress}"
            binding.bookReview.text = book.review

            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                itemClickListener(book)  // 클릭 시 리스너 호출
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(filteredBookList[position])
    }

    override fun getItemCount() = filteredBookList.size

    // Filterable 인터페이스 구현
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredBookList = if (filterPattern.isEmpty()) {
                    bookList
                } else {
                    bookList.filter {
                        it.title.lowercase().contains(filterPattern)
                    }
                }

                return FilterResults().apply {
                    values = filteredBookList
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBookList = results?.values as List<Book>
                notifyDataSetChanged()
            }
        }
    }
}
