package com.smhrd.bookor

import android.os.Bundle
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

class BookMemoActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookMemoBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MemoAdapter
    var editingPosition : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentDate = java.sql.Date(System.currentTimeMillis())

        // 데이터 불러와야 하는 부분 (임시 데이터 넣어둠)
        val memoList = listOf(
            BookMemo(currentDate, 111,"너무 재밌다. 50페이지를 순식간에 읽었다.")
        )

        // 메모 추가 버튼 클릭시 작동 로직
        binding.btnUdMemo.setOnClickListener {
            if (editingPosition != -1) {
                // 수정 중인 경우
                val updatedMemo = BookMemo(
                    memoList[editingPosition].date,
                    binding.etPages.text.toString().toInt(), // 필요하면 수정
                    binding.etMemo.text.toString()
                )
                // memoList 업데이트 로직 (예: 데이터베이스 업데이트)
                adapter.notifyItemChanged(editingPosition)
                editingPosition = -1 // 수정 완료 후 초기화
            } else {
                // 추가하는 경우
                val newMemo = BookMemo(
                    java.sql.Date(System.currentTimeMillis()),
                    binding.etPages.text.toString().toIntOrNull() ?: 0, // 숫자 변환, 실패 시 0
                    binding.etMemo.text.toString()
                )
//                lifecycleScope.launch {
//                    repository.insert(newMemo) // 데이터베이스에 추가
//                }
            }
            binding.etMemo.text?.clear() // 입력 필드 초기화
            binding.etPages.text?.clear()
        }

        recyclerView = binding.rvMemoList
        adapter = MemoAdapter(memoList) {
            memo, position ->
            editingPosition = position // 수정 중인 position 저장
            binding.etPages.setText(memo.lastPage.toString())
            binding.etMemo.setText(memo.memo) // 메모 내용 표시
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}