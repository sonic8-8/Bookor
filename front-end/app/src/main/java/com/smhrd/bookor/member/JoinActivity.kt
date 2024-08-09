package com.smhrd.bookor.member

import android.R.id
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.bookor.R
import org.json.JSONObject
import java.util.regex.Pattern


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

        // 사용자가 id 입력 칸을 벗어났을 때 id 유효성 검사
        // 첫번째 인자 : 쉽게 말하자면 inputId라는 EditeText객체(요소)를 가리키는 참조변수
        //             그렇기 때문에 해당 인자로 객체의 값를 변경하거나 다른 Activity에 보내는 등과 같이 사용할 수 있다.
        // 두번째 인자 : 해당 객체가 포커스를 가지고 있는지 없는지를 판단하는 boolean타입의 인자이다.
        // + 안드로이드에서는 button과 TextView와 같은 것들을 객체라고 표현한다.
        inputId.setOnFocusChangeListener { view, hasFocus ->
            Log.d("idFocus","id의 boolean값 : " + hasFocus.toString())

            // 칸을 벗어날 경우 hasFocus가 false로 바뀜
            // 아이디 유효성, 중복 검사
            if(!hasFocus){

                // DB에 조회하여 중복된 id값이 있는지 확인..


                Log.d("check","check")

                val idCheck = inputId.text.toString()

                // id 정규표현식 (영어와 숫자 조합으로 5글자 이상)
                val idValid = Pattern.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{5,}$", idCheck);


                Log.d("idValid", "현재 아이디 유효성 불린값 : " + idValid.toString())
                if(!idValid){
                    Toast.makeText(this,"영어와 숫자 조합으로 5글자 이상 입력해주세요.",Toast.LENGTH_SHORT).show()
                }else if (true){
                    Toast.makeText(this,"영어와 숫자 조합으로 5글자 이상 입력해주세요.",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"사용할 수 있는 아이디입니다.",Toast.LENGTH_SHORT).show()
                }

            }


        }

        // 사용자가 pw 입력 칸을 벗어났을 때 pw 유효성 검사
        inputPw.setOnFocusChangeListener { view, hasFocus ->
            Log.d("pwFocus","pw의 boolean값 : " + hasFocus.toString())

            // 칸을 벗어날 경우 hasFocus가 false로 바뀜
            if(!hasFocus){
                // 비밀번호 유효성 검사
            }

        }

        // "회원가입" 버튼을 클릭 시 실행됨
        btnJoinTry.setOnClickListener{

            // EditText에 입력되어 있는 값 가져오기
            val user_id = inputId.text.toString()
            val user_pw = inputPw.text.toString()
            val user_pwck = inputPwCheck.text.toString()
            val user_nick = inputNick.text.toString()



            // pw 정규표현식 (영어와 숫자 조합으로 7글자 이상)
            val pwValid = Pattern.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{5,}$", user_pw);



            // 사용자가 작성한 비밀번호와 비밀번호 확인이 서로 맞는가
            if(user_pw == user_pwck){
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

                Toast.makeText(this,"회원가입을 성공하였습니다.",Toast.LENGTH_SHORT).show()

                // intent@@
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)

            }else{
                Log.d("response", "비밀번호가 일치하지 않아요")
            }


        } // "회원가입" 리스너 끝

        // 이전 페이지 클릭 리스너
        btnJoinPrev.setOnClickListener {
            finish()
        }

    }
}