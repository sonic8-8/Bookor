package com.smhrd.bookor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.bookor.Goal.GoalMain2Activity
import com.smhrd.bookor.alarm.AlarmmainActivity
import com.smhrd.bookor.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val queue = Volley.newRequestQueue(this)
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private val bookList: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 알람 페이지 이동
        findViewById<ImageButton>(R.id.alrambtn).setOnClickListener {
            startActivity(Intent(this, AlarmmainActivity::class.java))
        }

        // 목표 설정
        binding.goalbtn.setOnClickListener {
            startActivity(Intent(this, GoalMain2Activity::class.java))
        }

        // 메인 목표 텍스트 값
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val goaltext = sharedPreferences.getString("goalText", null)
        val currentText = sharedPreferences.getString("currentText", null)

        // text 수정
        findViewById<TextView>(R.id.tvGoal).text = goaltext
        findViewById<TextView>(R.id.tvCurrent).text = currentText

        // 서버에서 책 목록 가져오기 (GET 요청)
        fetchBookList()

        // 책 추가 (POST 요청)
        binding.btnAddBook.setOnClickListener {
            addBook()
        }


        // RecyclerView 설정
        bookAdapter = BookAdapter(bookList) { book ->
            // 아이템 클릭 시 상세 화면으로 이동
            val intent = Intent(this, BookMemoActivity::class.java).apply {

        bookList = mutableListOf(
            Book(3,"The Great Gatsby", "100%", "별점: 5, 리뷰: Great!"),
            Book(2,"1984", "80%", "별점: 4, 리뷰: Interesting!"),
            Book(1,"To Kill a Mockingbird", "60%", "별점: 5, 리뷰: Thought-provoking!")
        )

        // BookAdapter를 생성할 때 클릭 리스너를 전달
        bookAdapter = BookAdapter(bookList) { book ->
            // 아이템 클릭 시 상세 화면으로 이동
            val intent = Intent(this,BookMemoActivity::class.java).apply {
                putExtra("BOOK_ID", book.id)

                putExtra("BOOK_title", book.title)
                putExtra("BOOK_pages", book.pages)
                putExtra("BOOK_review", book.review)
            }
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = bookAdapter

        // 검색 기능
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                bookAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun fetchBookList() {
        val url = "http://0.0.2.2:8085/api/book/listup" // 서버 IP 주소 및 포트 번호 입력
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val gson = Gson()
                bookList.clear()
                bookList.addAll(gson.fromJson(response.toString(), Array<Book>::class.java).toList())
                bookAdapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("Volley Error", error.toString())
                Toast.makeText(this, "책 목록을 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun addBook() {
        val bookTitle = binding.etBookTitle.text.toString()
        val bookPages = binding.etBookPages.text.toString().toIntOrNull() ?: 0 // 입력값이 숫자가 아닐 경우 0으로 처리

        if (bookTitle.isBlank()) {
            Toast.makeText(this, "책 제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val addUrl = "http://your-server-ip:8080/book/add"
        val newBook = Book(bookTitle, bookPages, "")
        val gson = Gson()
        val requestBody = gson.toJson(newBook)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, addUrl, JSONObject(requestBody),
            { response ->
                bookList.add(gson.fromJson(response.toString(), Book::class.java))
                bookAdapter.notifyDataSetChanged()
                binding.etBookTitle.text.clear()
                binding.etBookPages.text.clear()
            },
            { error ->
                Log.e("Volley Error", error.toString())
                Toast.makeText(this, "책 추가 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
    }
}
