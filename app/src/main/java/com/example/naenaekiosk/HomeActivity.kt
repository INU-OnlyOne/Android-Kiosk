package com.example.naenaekiosk

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.naenaekiosk.databinding.ActivityHomeBinding
import com.example.naenaekiosk.retrofit.*
import retrofit2.Call
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    lateinit var userInfo: SharedPreferences
    var num="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.waitingButton.setOnClickListener {
            val intent= Intent(this, WaitingActivity::class.java)
            intent.putExtra("waitingNum", num)
            startActivity(intent)
        }
        userInfo = getSharedPreferences("userInfo", 0)
        waitingList(resPhNum(userInfo.getString("resPhNum","").toString()))
        binding.totalWaiting.text=userInfo.getInt("totalWaiting", 0).toString()

        binding.manageButton!!.setOnClickListener {
            val intent= Intent(this, LockActivity::class.java)
            startActivity(intent)
        }
        Thread {
            var i=0
            while (!Thread.interrupted()) try {
                Thread.sleep(60000) //1분 간격으로 실행
                i=i+1
                runOnUiThread { Log.d("hy" ,i.toString()) }
            } catch (e: InterruptedException) {
                // error
            }
        }.start()
    }
    private fun waitingList(resPhNum: resPhNum){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.waitingList(resPhNum) ?:return

        call.enqueue(object : retrofit2.Callback<WaitList> {

            override fun onResponse(call: Call<WaitList>, response: Response<WaitList>) {
                Log.d("retrofit", "대기 팀수 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(response.body()?.result.isNullOrEmpty())
                    binding.totalWaiting.text="0"
                else {
                    num=response.body()!!.result.size.toString()
                    binding.totalWaiting.text=num
                }

            }
            override fun onFailure(call: Call<WaitList>, t: Throwable) {
                Log.d("retrofit", "대기 팀수  - 응답 실패 / t: $t")

            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        userInfo = getSharedPreferences("userInfo", 0)
        waitingList(resPhNum(userInfo.getString("resPhNum","").toString()))
    }

    override fun onResume() {
        super.onResume()
        userInfo = getSharedPreferences("userInfo", 0)
        waitingList(resPhNum(userInfo.getString("resPhNum","").toString()))
    }
}