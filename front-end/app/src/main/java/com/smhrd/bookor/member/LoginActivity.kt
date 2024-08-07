package com.smhrd.bookor.member

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.bookor.Goal.GoalMain2Activity
import com.smhrd.bookor.MainActivity
import com.smhrd.bookor.R
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        // id 찾아오기
        val inputId = findViewById<EditText>(R.id.etLoginId)
        val inputPw = findViewById<EditText>(R.id.etLoginPw)
        val btnLoginTry = findViewById<Button>(R.id.btnLoginTry)
        val btnLoginPrev = findViewById<Button>(R.id.btnLoginPrev)


        // "로그인" 버튼 누를 시 실행
        btnLoginTry.setOnClickListener{



            // 버튼 누를 시 text값 가져오기
            val id = inputId.text.toString()
            val pw = inputPw.text.toString()

            // JSON으로 묶기
            val jsonObject = JSONObject().apply {
                put("userId", id)
                put("userPw", pw)
            }

            val stringRequest = object : StringRequest(
                Request.Method.POST,
                "http://10.0.2.2:8085/api/login",
                {response->
                    Log.d("response", response.toString())

                    // Gson 라이브러리를 사용하여 JSON 형식의 데이터를 파싱한다.
                    val gson = Gson()
                    val member = gson.fromJson(response, Member::class.java)

                    // 객체에서 닉네임만 추출하기
                    val userNick = member.userNick
                    val memberId = member.id


                    val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putLong("memberId", memberId)
                    editor.apply()

                    // intent@@
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("userNick", userNick)
                    intent.putExtra("memberId", memberId.toString())

                    startActivity(intent)





                },
                {error->
                    Log.d("error", error.toString())
                }

            ) {
                //
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                //
                override fun getBody(): ByteArray {
                    return jsonObject.toString().toByteArray(charset("utf-8"))
                }
            }

            // queue에 add
            val queue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
            finish()
        }// "로그인" 리스너 끝

        // 이전 페이지 클릭 리스너
        btnLoginPrev.setOnClickListener {
            finish()
        }

    }
}