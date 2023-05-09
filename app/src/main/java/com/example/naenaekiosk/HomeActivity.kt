package com.example.naenaekiosk

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.naenaekiosk.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    lateinit var userInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.waitingButton.setOnClickListener {
            val intent= Intent(this, WaitingActivity::class.java)
            startActivity(intent)
        }
        userInfo = getSharedPreferences("userInfo", 0)
        binding.totalWaiting.text=userInfo.getInt("totalWaiting", 0).toString()
        Log.d("대기팀 - 홈화면", userInfo.getInt("totalWaiting", 0).toString())

        binding.manageButton!!.setOnClickListener {
            //todo 비번 입력 후 대기열 관리 페이지로 이동
            val intent= Intent(this, LockActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding.totalWaiting.text=userInfo.getInt("totalWaiting", 0).toString()
    }

    override fun onResume() {
        super.onResume()
        binding.totalWaiting.text=userInfo.getInt("totalWaiting", 0).toString()

    }
}