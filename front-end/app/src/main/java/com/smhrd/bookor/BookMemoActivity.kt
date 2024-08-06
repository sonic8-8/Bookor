package com.smhrd.bookor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.bookor.databinding.ActivityBookMemoBinding

class BookMemoActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookMemoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnAddMemo.setOnClickListener {
//            binding.etAddMemo.visibility
//        }
        val currentDate = java.sql.Date(System.currentTimeMillis())

        // 데이터 불러와야 하는 부분
        val memoList = listOf(
            BookMemo(currentDate, 111,"너무 재밌다. 50페이지를 순식간에 읽었다.")
        )

        recyclerView = binding.rvMemoList
        adapter = MemoAdapter(memoList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}