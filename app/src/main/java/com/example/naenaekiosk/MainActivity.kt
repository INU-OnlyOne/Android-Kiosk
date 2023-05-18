package com.example.naenaekiosk

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.naenaekiosk.databinding.ActivityMainBinding
import com.example.naenaekiosk.retrofit.*
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    lateinit var resPhNum:String
    lateinit var userInfo: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = getSharedPreferences("userInfo", 0)
        resPhNum= userInfo.getString("resPhNum","").toString()
        binding.startWaitingButton.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_TIME
            val formatted = current.format(formatter)
            startWaiting(StartWaiting(resPhNum, formatted))

        }
    }
    private fun startWaiting(StartWaiting: StartWaiting){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.startWaiting(StartWaiting) ?:return

        call.enqueue(object : retrofit2.Callback<StartWaiting> {

            override fun onResponse(call: Call<StartWaiting>, response: Response<StartWaiting>) {
                Log.d("retrofit", "대기 시작  - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val intent= Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<StartWaiting>, t: Throwable) {
                Log.d("retrofit", "대기 시작  - 응답 실패 / t: $t")

            }
        })
    }
}