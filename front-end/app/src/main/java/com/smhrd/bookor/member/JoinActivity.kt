package com.smhrd.bookor.member

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.bookor.R
import org.json.JSONObject

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_join)

        // id값을 변수에 담아두기
        val  inputId = findViewById<EditText>(R.id.etJoinId)
        val  inputPw = findViewById<EditText>(R.id.etJoinPw)
        val  inputPwCheck = findViewById<EditText>(R.id.etJoinPwCheck)
        val  inputNick = findViewById<EditText>(R.id.etJoinNick)
        val  btnJoinTry = findViewById<Button>(R.id.btnJoinTry)
        val  btnJoinPrev = findViewById<Button>(R.id.btnJoinPrev)

        // "회원가입" 버튼을 클릭 시 실행됨
        btnJoinTry.setOnClickListener{

            // EditText에 입력되어 있는 값 가져오기
            val user_id = inputId.text.toString()
            val user_pw = inputPw.text.toString()
            val user_nick = inputNick.text.toString()


            // 입력받은 데이터들을 JSON 형식으로 묶음
            /*
            밑 JSON 형식의 데이터들은 서버로 보낼 때
            서버에서 DTO로 받기 때문에 Key값을
            서버에 있는 DTO 변수명과 맞춰야한다.

            왜 JSON으로 보냈는데 서버에서는 DTO로 받을까?
            밑에는 다른 방식으로 받을 수 있는 방법들이다.
            1. Map(key와 value로 이루어져 있는 데이터 구조)
            Map은 모든 값(key가 아닌 value)을 Obejct타입(자바의 최상위 부모 클래스)으로 받기 때문에
            .. 구체적인 내용은 나중에 적어야지
            ->
            */
            val jsonObject = JSONObject().apply {
                put("userId", user_id)
                put("userPw", user_pw)
                put("userNick", user_nick)
            }

            // queue에 add할 Request 객체를 미리 생성해두기
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                "http://10.0.2.2:8085/api/join",
                {response->
                    Log.d("response", response.toString())
                },
                {error->
                    Log.d("error", error.toString())
                }
            ){
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

        } // "회원가입" 리스너 끝

        // 이전 페이지 클릭 리스너
        btnJoinPrev.setOnClickListener {
            finish()
        }

    }
}