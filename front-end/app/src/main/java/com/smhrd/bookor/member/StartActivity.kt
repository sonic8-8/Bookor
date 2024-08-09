package com.smhrd.bookor.member

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.bookor.MainActivity
import com.smhrd.bookor.R
import org.json.JSONObject

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        // id 찾아오기
        val inputId = findViewById<EditText>(R.id.etLoginId)
        val inputPw = findViewById<EditText>(R.id.etLoginPw)
        val btnLoginTry = findViewById<Button>(R.id.btnLoginTry)
        val tvJoinPage = findViewById<TextView>(R.id.tvJoinPage)
        val tvLoginFail = findViewById<TextView>(R.id.tvLoginFail)

        // 로그인을 시도하기 전에는 실패 메시지가 안보인다~
        tvLoginFail.visibility = View.GONE

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

                    // 서버에서 null값을 리턴할 경우 안드로이드에서는 빈문자열값("")으로 받는다~
                    // 로그인을 성공할시 동작하는 if문
                    if (response != ""){

                        Log.d("success", "success")

                        // Gson 라이브러리를 사용하여 JSON 형식의 데이터를 파싱한다.
                        val gson = Gson()
                        val member = gson.fromJson(response, Member::class.java)

                        // 객체에서 필요한 것들 추출하기
                        // val userNick = member.userNick
                        // val memberId = member.id

                        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        // editor.putLong("memberId", memberId)
                        editor.apply()

                        // intent@@
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    // 로그인을 실패할 시 동작하는 else문
                    else{
                        // 로그인을 실패했을 경우에는 확인 메시지가 뜬다~
                        tvLoginFail.visibility = View.VISIBLE

                        Log.d("fail","fail")
                    }

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
        }// "로그인" 리스너 끝



        // 회원가입 페이지로 이동하는 클릭 리스너
        tvJoinPage.setOnClickListener{

            // 로그인 실패 메시지가 떠있을 경우에는 가려주고 이동하기
            tvLoginFail.visibility = View.GONE

            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }
}