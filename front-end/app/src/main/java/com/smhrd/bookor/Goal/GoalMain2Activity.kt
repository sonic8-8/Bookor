package com.smhrd.bookor.Goal

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.bookor.R
import org.json.JSONObject
import org.json.JSONException

class GoalMain2Activity : AppCompatActivity() {

    private val TAG = "GoalMain2Activity"
    private val BASE_URL = "http://10.0.2.2:8085/api"

    // Activity 요소들은 onCreate() 내에서 초기화합니다.
    private lateinit var goalEditText: EditText
    private lateinit var currentEditText: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goal_main2)

        // findViewById를 onCreate() 내에서 호출합니다.
        goalEditText = findViewById(R.id.editTextGoal)
        currentEditText = findViewById(R.id.editTextCurrent)
        updateButton = findViewById(R.id.buttonUpdate)

        val queue: RequestQueue = Volley.newRequestQueue(this)

        updateButton.setOnClickListener {
            val user_goal = goalEditText.text.toString()
            val user_prgress = currentEditText.text.toString()

            val jsonBody = JSONObject().apply {
                put("user_goal", user_goal)
                put("user_prgress", user_prgress)
            }

            // POST 요청
            val stringRequest = object : StringRequest(
                Request.Method.POST, "$BASE_URL/Goal",
                Response.Listener { response ->
                    // 서버의 문자열 응답을 처리합니다.
                    Log.d(TAG, "POST Response: $response")

                    try {
                        // 응답 문자열을 JSON으로 변환합니다.
                        val jsonResponse = JSONObject(response)
                        // JSON 응답 처리 로직 추가
                        Log.d(TAG, "JSON Response: $jsonResponse")
                    } catch (e: JSONException) {
                        Log.e(TAG, "Response is not a valid JSON: ${e.message}")
                    }
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "POST Error: ${error.message}")
                }
            ) {
                override fun getBody(): ByteArray {
                    return jsonBody.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }

            queue.add(stringRequest)
        }
    }
}
