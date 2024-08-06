package com.smhrd.bookor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.smhrd.bookor.databinding.ActivityBookMemoBinding

class BookMemoActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val memoList
        val adapter = MemoAdapter()
        binding.btnAddMemo.setOnClickListener {
            binding.etAddMemo.visibility

        }
    }
}