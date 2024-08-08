package com.smhrd.bookor

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.bookor.databinding.ActivityBookMemoBinding
import org.json.JSONObject

class BookMemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookMemoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemoAdapter
    private var editingPosition: Int = -1
    private val queue = Volley.newRequestQueue(this)
    private var bookId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getLongExtra("BOOK_ID", -1)

        recyclerView = binding.rvMemoList
        adapter = MemoAdapter(mutableListOf()) { memo, position ->
            editingPosition = position
            binding.etPages.setText(memo.lastPage.toString())
            binding.etMemo.setText(memo.memo)
            binding.btnUdMemo.text = "메모 수정"
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnUdMemo.setOnClickListener {
            if (editingPosition != -1) {
                updateMemo(adapter.getItem(editingPosition).memoId)
            } else {
                addMemo()
            }
        }

        fetchMemoList()
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
