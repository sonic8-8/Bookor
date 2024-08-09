package com.smhrd.bookor

import VolleyMultipartRequest
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.smhrd.bookor.Goal.GoalMain2Activity
import com.smhrd.bookor.alarm.AlarmmainActivity
import com.smhrd.bookor.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookList: MutableList<Book>
    private lateinit var userImageView: ImageView
    private lateinit var changeProfileButton: Button

    // ActivityResultLauncher 선언
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 권한 요청을 위한 ActivityResultLauncher 초기화
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val readMediaImagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
            val readExternalStorageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            if (readMediaImagesGranted || readExternalStorageGranted) {
                openGallery()
            } else {
                showPermissionDeniedDialog()
            }
        }

        // ActivityResultLauncher 초기화
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                userImageView.setImageURI(imageUri)
                // 이미지 서버로 전송
                val bitmap = (userImageView.drawable as BitmapDrawable).bitmap
                val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                val memberId = sharedPreferences.getLong("memberId", -1L)
                if (memberId != -1L) {
                    uploadImageToServer(bitmap, memberId) // memberId를 전달
                } else {
                    Toast.makeText(this, "Member ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // View 초기화
        userImageView = findViewById(R.id.iv_user)
        changeProfileButton = findViewById(R.id.userimgbtn)
        val alarmbtn = findViewById<ImageButton>(R.id.alrambtn)

        // 버튼 클릭 리스너 설정
        changeProfileButton.setOnClickListener {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }
                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) || shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 권한 설명을 보여줄 필요가 있으면 설명 후 권한 요청
                    showPermissionRationaleDialog()
                }
                else -> {
                    // 권한 요청
                    requestPermissionLauncher.launch(getRequiredPermissions())
                }
            }
        }

        alarmbtn.setOnClickListener {
            val intent = Intent(this, AlarmmainActivity::class.java)
            startActivity(intent)
        }

        binding.goalbtn.setOnClickListener {
            val intent = Intent(this, GoalMain2Activity::class.java)
            startActivity(intent)
        }

        // SharedPreferences에서 텍스트 값 불러오기
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val goaltext = sharedPreferences.getString("goalText", null)
        val currentText = sharedPreferences.getString("currentText", null)

        findViewById<TextView>(R.id.tvGoal).text = goaltext
        findViewById<TextView>(R.id.tvCurrent).text = currentText

        // 책 목록 초기화
        bookList = mutableListOf(
            Book("The Great Gatsby", "100%", "별점: 5, 리뷰: Great!"),
            Book("1984", "80%", "별점: 4, 리뷰: Interesting!"),
            Book("To Kill a Mockingbird", "60%", "별점: 5, 리뷰: Thought-provoking!")
        )

        // BookAdapter를 생성할 때 클릭 리스너를 전달
        bookAdapter = BookAdapter(bookList) { book ->
            val intent = Intent(this, BookMemoActivity::class.java).apply {
                putExtra("BOOK_title", book.title)
                putExtra("BOOK_review", book.review)
                putExtra("BOOK_progress", book.progress)
            }
            startActivity(intent)
        }

        // RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = bookAdapter

        // SearchView 리스너 설정
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

    // 이미지 기능
    private fun openGallery() {
        Log.d("MainActivity", "Opening gallery")
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 요청")
            .setMessage("이미지 접근 권한이 필요합니다. 권한을 허용해주세요.")
            .setPositiveButton("확인") { _, _ ->
                requestPermissionLauncher.launch(getRequiredPermissions())
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 거부됨")
            .setMessage("이미지 접근 권한이 거부되었습니다. 설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun uploadImageToServer(bitmap: Bitmap, memberId: Long) {
        val url = "http://10.0.2.2:8085/api/upload" // 서버의 업로드 엔드포인트

        val volleyMultipartRequest = object : VolleyMultipartRequest(
            Request.Method.POST, url,
            Response.Listener<NetworkResponse> { response ->
                val resultResponse = String(response.data, Charsets.UTF_8) // UTF-8로 변환
                Log.d("MainActivity", "Server response: $resultResponse")

                // URL 파싱 및 유효성 검사
                if (resultResponse.startsWith("Image uploaded successfully: ")) {
                    val imageId = resultResponse.removePrefix("Image uploaded successfully: ").trim()
                    loadImageFromServer(imageId)
                } else {
                    Toast.makeText(this@MainActivity, "Invalid server response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                Log.e("MainActivity", "Error uploading image: ${error.message}")
                // 에러 처리
                Toast.makeText(this@MainActivity, "이미지 업로드 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getByteData(): Map<String, DataPart> {
                Log.d("MainActivity", "Preparing image data for upload")
                val params: MutableMap<String, DataPart> = HashMap()
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                params["image"] = DataPart("image.jpg", byteArrayOutputStream.toByteArray())
                return params
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = memberId.toString() // Long 값을 문자열로 변환하여 전달
                return params
            }
        }

        Log.d("MainActivity", "Starting upload")
        // Adding request to request queue
        Volley.newRequestQueue(this).add(volleyMultipartRequest)
    }

    private fun loadImageFromServer(imageId: String) {
        val url = "http://10.0.2.2:8085/api/image/$imageId"

        val imageRequest = ImageRequest(url,
            { response ->
                userImageView.setImageBitmap(response)
            },
            0, 0, null, Bitmap.Config.RGB_565,
            { error ->
                Log.e("MainActivity", "Error loading image: ${error.message}")
                Toast.makeText(this@MainActivity, "Error loading image: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(imageRequest)
    }
}
