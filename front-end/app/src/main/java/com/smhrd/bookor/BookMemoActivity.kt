package com.smhrd.bookor

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.bookor.databinding.ActivityBookMemoBinding
import kotlinx.coroutines.launch
import java.sql.Date
import org.json.JSONObject

class BookMemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookMemoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemoAdapter
    private var editingPosition: Int = -1
    private lateinit var queue: RequestQueue
    private var bookId: Int = -1
    private lateinit var bookTitle: String
    private lateinit var bookProgress: String
    private lateinit var bookReview: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)

        // Intent로부터 책 정보와 ID 가져오기
        bookId = intent.getIntExtra("BOOK_ID", -1)
        bookTitle = intent.getStringExtra("BOOK_TITLE") ?: "Unknown Title"
        bookProgress = intent.getStringExtra("BOOK_PROGRESS") ?: "Unknown Progress"
        bookReview = intent.getStringExtra("BOOK_REVIEW") ?: "Unknown Review"

        // 책 정보를 UI에 표시
        binding.tvBookName.text = bookTitle
        binding.tvProgressText1.text = "진행률 : $bookProgress"
        binding.tvReviewContent.text = bookReview

        Log.d("BookMemoActivity", "Book Title: $bookTitle, Progress: $bookProgress, Review: $bookReview")


        val currentDate = Date(System.currentTimeMillis())

        // 데이터 불러와야 하는 부분 (임시 데이터 넣어둠)
        val memoList = mutableListOf(
            BookMemo(currentDate, 111, "너무 재밌다. 50페이지를 순식간에 읽었다.")
        )

        // 메모 추가 버튼 클릭 시 작동 로직
        binding.btnUdMemo.setOnClickListener {
            if (editingPosition != -1) {
                // 수정 중인 경우
                val updatedMemo = BookMemo(
                    memoList[editingPosition].date,
                    binding.etPages.text.toString().toInt(),
                    binding.etMemo.text.toString()
                )
                // memoList 업데이트 로직 (예: 데이터베이스 업데이트)
                memoList[editingPosition] = updatedMemo
                adapter.notifyItemChanged(editingPosition)
                editingPosition = -1 // 수정 완료 후 초기화
            } else {
                // 추가하는 경우
                val newMemo = BookMemo(
                    Date(System.currentTimeMillis()),
                    binding.etPages.text.toString().toIntOrNull() ?: 0,
                    binding.etMemo.text.toString()
                )
                memoList.add(newMemo)
                // lifecycleScope.launch { repository.insert(newMemo) } // 데이터베이스에 추가
                adapter.notifyItemInserted(memoList.size - 1)
            }
            binding.etMemo.text?.clear() // 입력 필드 초기화
            binding.etPages.text?.clear()
        }

        // RecyclerView 설정
        binding.rvMemoList.layoutManager = LinearLayoutManager(this)
        adapter = MemoAdapter(memoList) { memo, position ->
            editingPosition = position // 수정 중인 position 저장
            binding.etPages.setText(memo.lastPage.toString())
            binding.etMemo.setText(memo.memo)
        }

        binding.rvMemoList.adapter = adapter

        // ----------------------------------------
//        recyclerView = binding.rvMemoList
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        binding.btnUdMemo.setOnClickListener {
//            if (editingPosition != -1) {
//                addMemo()
//            }
//        }
//
//        fetchMemoList()
    }

    private fun fetchMemoList() {
        val url = "http://0.0.2.2:8085/api/memo/listup?bookId=$bookId"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val gson = Gson()
                val memoList = gson.fromJson(response.toString(), Array<BookMemo>::class.java).toList()
                adapter.updateMemoList(memoList)
            },
            { error ->
                Log.e("Volley Error", error.toString())
                Toast.makeText(this, "메모 목록을 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun addMemo() {
        val url = "http://0.0.2.2:8085/api/memo/add"
        val newMemo = BookMemo(java.sql.Date(System.currentTimeMillis()), binding.etPages.text.toString().toIntOrNull() ?: 0 , binding.etMemo.text.toString())
        val gson = Gson()
        val requestBody = gson.toJson(newMemo)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(requestBody),
            { response ->
                val addedMemo = gson.fromJson(response.toString(), BookMemo::class.java)
                adapter.addMemo(addedMemo)
                binding.etMemo.text?.clear()
                binding.etPages.text?.clear()
                binding.btnUdMemo.text = "메모 추가"
            },
            { error ->
                Log.e("Volley Error", error.toString())
                Toast.makeText(this, "메모 추가 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)

    }

    private fun updateMemo(memoId : Long) {
        val url = "http://0.0.2.2:8085/api/memo/update/$memoId"
        val originalMemo = adapter.getItem(editingPosition)
        val updatedMemo = BookMemo(originalMemo.date,  binding.etPages.text.toString().toIntOrNull() ?: 0, binding.etMemo.text.toString())
        val gson = Gson()
        val requestBody = gson.toJson(updatedMemo)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, JSONObject(requestBody),
            { response ->
                val updatedMemoFromServer = gson.fromJson(response.toString(), BookMemo::class.java)
                adapter.updateMemo(editingPosition, updatedMemoFromServer)
                editingPosition = -1
                binding.etMemo.text?.clear()
                binding.etPages.text?.clear()
                binding.btnUdMemo.text = "메모 추가"
            },
            { error ->
                Log.e("Volley Error", error.toString())
                Toast.makeText(this, "메모 수정 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
    }
}
