package com.example.naenaekiosk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.naenaekiosk.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.waitingButton.setOnClickListener {
            val intent= Intent(this, WaitingActivity::class.java)
            startActivity(intent)
        }
        binding.manageButton!!.setOnClickListener {
            //todo 비번 입력 후 대기열 관리 페이지로 이동
            val intent= Intent(this, LockActivity::class.java)
            startActivity(intent)
        }
    }
}