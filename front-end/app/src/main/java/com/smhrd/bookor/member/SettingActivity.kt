package com.smhrd.bookor.member

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.bookor.R
import org.json.JSONObject

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 닉네임도 갖고올것

        // id값 갖고옴
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // 회원탈퇴 버튼 클릭 시
        btnDelete.setOnClickListener {

            // 현재 임시로 데이터를 묶어서 보냄
            val jsonObject = JSONObject().apply {
                put("userId", "1")
                put("userPw", "1")
                put("userNick", "1")
            }

            val stringRequest = object : StringRequest(
                Request.Method.POST,
                "http://10.0.2.2:8085/api/delete",
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

            val queue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
        }



        // 로그아웃 버튼 클릭 시
        btnLogout.setOnClickListener {

        }


    }
}