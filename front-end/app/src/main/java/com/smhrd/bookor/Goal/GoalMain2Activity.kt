package com.smhrd.bookor.Goal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.bookor.MainActivity
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


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goal_main2)
        // findViewById를 onCreate() 내에서 호출합니다.
        goalEditText = findViewById(R.id.editTextGoal)
        currentEditText = findViewById(R.id.editTextCurrent)
        updateButton = findViewById(R.id.buttonUpdate)

        val goaltext = findViewById<TextView>(R.id.textViewGoal)
        val progresstext = findViewById<TextView>(R.id.textViewProgress)

        findViewById<Button>(R.id.goalrevbtn).setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


        val goalshare = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val goaltextView = goalshare.getString("goalText",null)
        val currentTextView = goalshare.getString("currentText",null)

        goaltext.text = goaltextView
        progresstext.text = currentTextView


        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val memberId = sharedPreferences.getLong("memberId", -1L)

        if (memberId == -1L) {
            Log.e(TAG, "Invalid memberId")
            finish()
            return
        }



        val queue: RequestQueue = Volley.newRequestQueue(this)

        updateButton.setOnClickListener {
            val user_goal = goalEditText.text.toString()
            val user_prgress = currentEditText.text.toString()

            val jsonBody = JSONObject().apply {
                put("user_goal", user_goal)
                put("user_prgress", user_prgress)
                put("memberId" ,memberId)
            }

            // POST 요청
            val stringRequest = object : StringRequest(
                Request.Method.POST, "$BASE_URL/goal",
                Response.Listener { response ->
                    // 서버의 문자열 응답을 처리합니다.
                    Log.d(TAG, "POST Response: $response")

                    try {
                        // 응답 문자열을 JSON으로 변환합니다.
                        val jsonResponse = JSONObject(response)
                        // JSON 응답 처리 로직 추가
                        goalEditText.setText(jsonResponse.getString("user_goal"))
                        currentEditText.setText(jsonResponse.getString("user_prgress"))

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




            val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("goalText", user_goal)
            editor.putString("currentText", user_prgress)
            editor.apply()


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }



    }
}
