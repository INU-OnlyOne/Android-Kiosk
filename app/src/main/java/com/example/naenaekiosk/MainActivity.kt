package com.example.naenaekiosk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naenaekiosk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startWaitingButton.setOnClickListener {
            //todo 영업 시작하는 코드 작성
            val intent= Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}