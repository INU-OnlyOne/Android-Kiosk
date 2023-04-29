package com.example.naenaekiosk

import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.naenaekiosk.databinding.ActivityManageWaitingBinding
import com.example.naenaekiosk.retrofit.Customer


class ManageWaitingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageWaitingBinding
    var resName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityManageWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //todo 대기자 명단 레트로핏핏
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
            headCount.text=Customer.headCount.toString()
            callButton.setOnClickListener {
                try {
                    //전송
                    val phoneNum=Customer.userPhone.replace("[^0-9]", "") // 숫자를 제외한 모든 문자 제거
                    val smsManager: SmsManager = SmsManager.getDefault()
                    val txt="[내 자리 내놔]\n${resName} 입장이 얼마 남지 않았습니다. 가게 앞에서 대기해주세요!"
                    smsManager.sendTextMessage(phoneNum, null, txt, null, null)
                    Toast.makeText(applicationContext, "전송 완료!", Toast.LENGTH_LONG).show()
                    //todo 호출 연결
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "문자 전송 실패", Toast.LENGTH_LONG).show()
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
                //todo 레트로핏
            }
            timeOutButton.setOnClickListener {
                //todo 레트로핏
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