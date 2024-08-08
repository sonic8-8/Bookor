package com.smhrd.bookor

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.bookor.databinding.ActivityBookMemoBinding
import kotlinx.coroutines.launch
import java.sql.Date

class BookMemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookMemoBinding
    private lateinit var adapter: MemoAdapter
    private var editingPosition: Int = -1
    private var bookId: Int = -1
    private lateinit var bookTitle: String
    private lateinit var bookProgress: String
    private lateinit var bookReview: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.etMemo.setText(memo.memo) // 메모 내용 표시
        }
        binding.rvMemoList.adapter = adapter
    }
}
