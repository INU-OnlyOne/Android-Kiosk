package com.example.naenaekiosk

import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naenaekiosk.databinding.ActivityManageWaitingBinding
import com.example.naenaekiosk.retrofit.Customer


class ManageWaitingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageWaitingBinding
    var resName=""
    val WaitingList = ArrayList<Customer>()
    val WaitingList2 = ArrayList<Customer>()
    var waitingNum=0
    lateinit var userInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityManageWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = getSharedPreferences("userInfo", 0)
        val range = (1000 .. 9999)
        val range2 = (0 .. 8)
        val range3 = (2 .. 8)
        val keywordList = listOf("#테라스석", "상관없어요", "#룸", "#프라이빗", "#단체석", "#에어컨앞", "#루프탑", "#포토존", "#바테이블")
        for(i in 1 until userInfo.getInt("totalWaiting", 0)){
            WaitingList.add(Customer( waitingNum,"010-${range.random()}-${range.random()}", range3.random(), keywordList.get(range2.random()), "15:22:49"))
            waitingNum += 1
        }

        WaitingList.add(Customer( waitingNum,"010-1234-5678", 3, "#루프탑", "15:27:49"))
        waitingNum += 1

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = WaitingAdapter(WaitingList)

        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.adapter = CalledAdapter(WaitingList2)
    }
    fun deleteWaitingList1(customer: Customer) {
        WaitingList.remove(customer)
        WaitingList2.add(customer)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        binding.recyclerView2.adapter?.notifyDataSetChanged()

    }
    fun deleteWaitingList2(customer: Customer) {
        WaitingList2.remove(customer)
        binding.recyclerView2.adapter?.notifyDataSetChanged()
        val now= userInfo.getInt("totalWaiting", 0)
        userInfo.edit().putInt("totalWaiting", now-1).apply()
    }

    inner class WaitingViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var customer: Customer
        private val waitingNum:TextView=itemView.findViewById(R.id.waitingNum)
        private val phoneNum : TextView = itemView.findViewById(R.id.waitingPhoneNumber)
        private val keyword:TextView=itemView.findViewById(R.id.keyword1)
        private val headCount:TextView=itemView.findViewById(R.id.textView5)
        private val callButton: Button =itemView.findViewById(R.id.button)

        fun bind(Customer: Customer){
            this.customer=Customer
            waitingNum.text=Customer.waitingNum.toString()
            phoneNum.text=Customer.userPhone
            keyword.text=Customer.keyword
            headCount.text=Customer.headCount.toString()+"인"
            callButton.setOnClickListener {
                deleteWaitingList1(Customer)
                try {
                    //전송
                    val phoneNum=Customer.userPhone.replace("[^0-9]", "") // 숫자를 제외한 모든 문자 제거
                    val smsManager: SmsManager = SmsManager.getDefault()
                    val txt="[내 자리 내놔]\n${resName} 입장이 얼마 남지 않았습니다. 가게 앞에서 대기해주세요!"
                    smsManager.sendTextMessage(phoneNum, null, txt, null, null)
                    Toast.makeText(applicationContext, "전송 완료!", Toast.LENGTH_LONG).show()
                    //todo 호출 연결
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "문자 완료!", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

        }

    }
    inner class WaitingAdapter(private val list:List<Customer>): RecyclerView.Adapter<WaitingViewHolder>(){
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
        private lateinit var customer: Customer
        private val waitingNum:TextView=itemView.findViewById(R.id.waitingNum)
        private val phoneNum : TextView = itemView.findViewById(R.id.waitingPhoneNumber)
        private val keyword:TextView=itemView.findViewById(R.id.keyword1)
        private val headCount:TextView=itemView.findViewById(R.id.keyword3)
        private val callTime:TextView=itemView.findViewById(R.id.textView27)
        private val finButton: Button =itemView.findViewById(R.id.button)
        private val timeOutButton: Button =itemView.findViewById(R.id.button2)

        fun bind(Customer: Customer){
            this.customer=Customer
            waitingNum.text=Customer.waitingNum.toString()
            phoneNum.text=Customer.userPhone
            keyword.text=Customer.keyword
            headCount.text=Customer.headCount.toString()
            callTime.text=Customer.callTime

            finButton.setOnClickListener {
                deleteWaitingList2(Customer)
            }
            timeOutButton.setOnClickListener {
                deleteWaitingList2(Customer)
                //todo 타임아웃
            }

        }


    }
    inner class CalledAdapter(private val list:List<Customer>): RecyclerView.Adapter<CalledViewHolder>(){
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
}