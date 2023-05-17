package com.example.naenaekiosk

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.naenaekiosk.databinding.ActivitySignInBinding
import kotlin.properties.Delegates

class SignInActivity : AppCompatActivity(), ConfirmDialogInterface {

    private lateinit var binding: ActivitySignInBinding
    private var userId =""
    lateinit var userInfo: SharedPreferences
    private var isMember=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //연정식당 더미데이터
        userInfo = getSharedPreferences("userInfo", 0)
        userInfo.edit().putString("resID", "18").apply()
        userInfo.edit().putString("resPhNum", "032 525 3745").apply()
        userInfo.edit().putString("resPW", "pw0002").apply()
        userInfo.edit().putString("resPIN", "1234").apply()
        userInfo.edit().putInt("totalWaiting", 0).apply()
        userInfo.edit().putString("resKeyWord", "익숙한맛, 넓은주차장, 가족끼리").apply()
        userId = userInfo.getString("userInfo", "032-811-7877").toString()

        Log.d("대기팀 - 로그인", userInfo.getInt("totalWaiting", 0).toString())

        binding.signUp.setOnClickListener {
            val intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener{
            //todo 로그인 정보 확인해서 isMember에 넘기기

            if(isMember){
                //todo 유저 정보아이디 저장
                var userId=binding.editTextTextPersonName.text.toString()
                userInfo.edit().putString("userId", userId).apply()
                //todo 유저 핀 정보 수정
                userInfo.edit().putString("userPIN", "1234").apply()
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{ //등록된 계정이 아니면 경고
                val dialog = CustomDialog(this@SignInActivity, "잘못된 아이디 또는 비밀번호입니다." , 0, 1)
                dialog.isCancelable = true
                dialog.show(this.supportFragmentManager,"ConfirmDialog")
            }
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
    }
}