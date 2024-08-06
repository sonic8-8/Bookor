package com.smhrd.bookor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemoAdapter(private val memoList: List<BookMemo>) :
    RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    class MemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date : TextView = itemView.findViewById(R.id.tvMemoDate)
        val pages : TextView = itemView.findViewById(R.id.tvReadedPages)
        val memoNote : TextView = itemView.findViewById(R.id.tvMemoNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_memo_list_content, parent, false)
        return MemoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val currentItem = memoList[position]
        holder.date.text = currentItem.date.toString()
        holder.pages.text = "~" + currentItem.lastPage?.toString() + "pages"
        holder.memoNote.text = currentItem.memo
    }

    override fun getItemCount() = memoList.size
}