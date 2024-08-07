package com.smhrd.bookor

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smhrd.bookor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookList: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 닉네임 받아오기
        getIntent().getStringExtra("userNick")
        val userNick = intent.getStringExtra("userNick")

        Log.d("userNick", userNick.toString())


        // 받은 닉네임 텍스트뷰에 표시
        val loginNick = findViewById<TextView>(R.id.LoginNick)
         loginNick.text = userNick



        // RecyclerView 설정
        bookList = mutableListOf(
            Book("The Great Gatsby", "100%", "별점: 5, 리뷰: Great!"),
            Book("1984", "80%", "별점: 4, 리뷰: Interesting!"),
            Book("To Kill a Mockingbird", "60%", "별점: 5, 리뷰: Thought-provoking!")
        )

        bookAdapter = BookAdapter(bookList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = bookAdapter
    }
}