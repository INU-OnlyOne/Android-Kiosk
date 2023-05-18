package com.example.naenaekiosk

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naenaekiosk.databinding.ActivityManageWaitingBinding
import com.example.naenaekiosk.retrofit.*
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ManageWaitingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageWaitingBinding
    var resName=""
    lateinit var resId:String
    lateinit var resPhNum:String
    lateinit var userInfo: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityManageWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = getSharedPreferences("userInfo", 0)
        resId= userInfo.getString("resID","0").toString()
        resPhNum= userInfo.getString("resPhNum","").toString()
        update()
        binding.button3.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_TIME
            val formatted = current.format(formatter)
            endWaiting(EndWaiting(resPhNum, formatted))
        }
    }


    inner class WaitingViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var customer: WaitListItem
        private val waitingNum:TextView=itemView.findViewById(R.id.waitingNum)
        private val phoneNum : TextView = itemView.findViewById(R.id.waitingPhoneNumber)
        private val keyword:TextView=itemView.findViewById(R.id.keyword1)
        private val headCount:TextView=itemView.findViewById(R.id.textView5)
        private val callButton: Button =itemView.findViewById(R.id.button)

        fun bind(Customer: WaitListItem){
            this.customer=Customer
            waitingNum.text=Customer.WaitIndex .toString()
            phoneNum.text=Customer.UserPhone
            keyword.text=Customer.keyword
            headCount.text=Customer.WaitHeadcount .toString()+"인"
            callButton.setOnClickListener {

                try {
                    //전송
                    val phoneNum=Customer.UserPhone.replace("[^0-9]", "") // 숫자를 제외한 모든 문자 제거
                    val smsManager: SmsManager = SmsManager.getDefault()
                    val txt="[내 자리 내놔]\n${resName} 입장이 얼마 남지 않았습니다. 가게 앞에서 대기해주세요!"
                    smsManager.sendTextMessage(phoneNum, null, txt, null, null)
                    Toast.makeText(applicationContext, "전송 완료!", Toast.LENGTH_LONG).show()
                    //todo 호출 연결
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "문자 전송 실패!", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

        }

    }
    inner class WaitingAdapter(private val list:List<WaitListItem>): RecyclerView.Adapter<WaitingViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaitingViewHolder {
            val view=layoutInflater.inflate(R.layout.item_waiting_list, parent, false)
            return WaitingViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: WaitingViewHolder, position: Int) {
            val post = list[position]
            holder.bind(post)
        }
    }
    inner class CalledViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var customer: CallListItem
        private val waitingNum:TextView=itemView.findViewById(R.id.waitingNum)
        private val phoneNum : TextView = itemView.findViewById(R.id.waitingPhoneNumber)
        private val keyword:TextView=itemView.findViewById(R.id.keyword1)
        private val headCount:TextView=itemView.findViewById(R.id.keyword3)
        private val callTime:TextView=itemView.findViewById(R.id.textView27)
        private val finButton: Button =itemView.findViewById(R.id.button)
        private val timeOutButton: Button =itemView.findViewById(R.id.button2)

        fun bind(Customer: CallListItem){
            this.customer=Customer
            waitingNum.text=Customer.WaitedIdx .toString()
            phoneNum.text=Customer.UserPhone
            keyword.text=Customer.WaitSeat
            headCount.text=Customer.WaitHeadcount.toString()
            callTime.text=Customer.acceptedTime

            finButton.setOnClickListener {
                acceptWaiting(AcceptWaiting(Customer.WaitedIdx.toString(), resPhNum, Customer.UserPhone))
            }
            timeOutButton.setOnClickListener {
                rejectWaiting(RejectWaiting(resId, Customer.UserPhone))
            }

        }


    }
    inner class CalledAdapter(private val list:List<CallListItem>): RecyclerView.Adapter<CalledViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalledViewHolder {
            val view=layoutInflater.inflate(R.layout.item_waiting_list2, parent, false)
            return CalledViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: CalledViewHolder, position: Int) {
            val post = list[position]
            holder.bind(post)
        }
    }

    private fun waitingList(resPhNum: resPhNum){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.waitingList(resPhNum) ?:return

        call.enqueue(object : retrofit2.Callback<WaitList> {

            override fun onResponse(call: Call<WaitList>, response: Response<WaitList>) {
                Log.d("retrofit", "대기 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(!response.body()?.result.isNullOrEmpty()){
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@ManageWaitingActivity)
                    binding.recyclerView.adapter = WaitingAdapter(response.body()!!.result)
                }
            }
            override fun onFailure(call: Call<WaitList>, t: Throwable) {
                Log.d("retrofit", "대기 리스트  - 응답 실패 / t: $t")
            }
        })
    }
    private fun callList(resPhNum: resPhNum){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.callList(resPhNum) ?:return

        call.enqueue(object : retrofit2.Callback<CallList> {

            override fun onResponse(call: Call<CallList>, response: Response<CallList>) {
                Log.d("retrofit", "호출 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(!response.body()?.message.isNullOrEmpty()){
                    binding.recyclerView2.layoutManager = LinearLayoutManager(this@ManageWaitingActivity)
                    binding.recyclerView2.adapter = CalledAdapter(response.body()?.message!!)
                }
            }
            override fun onFailure(call: Call<CallList>, t: Throwable) {
                Log.d("retrofit", "호출 리스트  - 응답 실패 / t: $t")

            }
        })
    }
    private fun acceptWaiting(AcceptWaiting: AcceptWaiting){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.acceptWaiting(AcceptWaiting) ?:return

        call.enqueue(object : retrofit2.Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("retrofit", "호출  - 응답 성공 / t : ${response.raw()} ${response.body()}")

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("retrofit", "호출  - 응답 실패 / t: $t")

            }
        })
    }
    private fun rejectWaiting(RejectWaiting: RejectWaiting){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.rejectWaiting(RejectWaiting) ?:return

        call.enqueue(object : retrofit2.Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("retrofit", "거절  - 응답 성공 / t : ${response.raw()} ${response.body()}")

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("retrofit", "거절  - 응답 실패 / t: $t")

            }
        })
    }
    private fun endWaiting(EndWaiting: EndWaiting){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.endWaiting(EndWaiting) ?:return

        call.enqueue(object : retrofit2.Callback<EndWaiting> {

            override fun onResponse(call: Call<EndWaiting>, response: Response<EndWaiting>) {
                Log.d("retrofit", "대기 마감  - 응답 성공 / t : ${response.raw()} ${response.body()}")

            }

            override fun onFailure(call: Call<EndWaiting>, t: Throwable) {
                Log.d("retrofit", "대기 마감  - 응답 실패 / t: $t")

            }
        })
    }
    fun update(){
        waitingList(resPhNum(userInfo.getString("resPhNum","").toString()))
        callList(resPhNum(userInfo.getString("resPhNum","").toString()))
    }
    override fun onRestart() {
        super.onRestart()
        update()
    }

    override fun onResume() {
        super.onResume()
        update()
    }
}