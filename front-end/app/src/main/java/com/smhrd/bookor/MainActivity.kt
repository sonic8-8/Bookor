package com.smhrd.bookor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smhrd.bookor.Goal.GoalMain2Activity
import com.smhrd.bookor.alarm.AlarmmainActivity
import com.smhrd.bookor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookList: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alarmbtn = findViewById<ImageButton>(R.id.alrambtn)
        //알람 페이지 이동
        alarmbtn .setOnClickListener{
            val intent = Intent(this,AlarmmainActivity::class.java)
            startActivity(intent)
        }


        //목표 설정
        binding.goalbtn.setOnClickListener{
            val intent = Intent(this,GoalMain2Activity::class.java)
            startActivity(intent)
        }

        //메인 목표 텍스트 값
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val goaltext = sharedPreferences.getString("goalText",null)
        val currentText = sharedPreferences.getString("currentText",null)


        //text수정
        findViewById<TextView>(R.id.tvGoal).text = goaltext
        findViewById<TextView>(R.id.tvCurrent).text = currentText



        bookList = mutableListOf(
            Book("The Great Gatsby", "100%", "별점: 5, 리뷰: Great!"),
            Book("1984", "80%", "별점: 4, 리뷰: Interesting!"),
            Book("To Kill a Mockingbird", "60%", "별점: 5, 리뷰: Thought-provoking!")
        )

        // BookAdapter를 생성할 때 클릭 리스너를 전달
        bookAdapter = BookAdapter(bookList) { book ->
            // 아이템 클릭 시 상세 화면으로 이동
            val intent = Intent(this,BookMemoActivity::class.java).apply {
                putExtra("BOOK_title", book.title)
                putExtra("BOOK_review", book.review)
                putExtra("BOOK_title", book.progress)// 필요한 데이터 전달
            }
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = bookAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                bookAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                bookAdapter.filter.filter(newText)
                return true
            }
        })
    }
}

