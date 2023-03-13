package com.example.naenaekiosk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naenaekiosk.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signUp!!.setOnClickListener {
            val intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener{
            //todo 로그인 정보 확인
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}