package com.example.naenaekiosk

import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.naenaekiosk.databinding.ActivityWaitingBinding
import com.example.naenaekiosk.retrofit.*
import com.google.android.material.chip.Chip

import retrofit2.Call
import retrofit2.Response

class WaitingActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityWaitingBinding
    private lateinit var dialog1:CustomDialog
    private var phone1="0000"
    private var phone2="0000"
    private var phone3="010"
    var numberOfPeople=2
    private var searKeyword=""
    private var isSeatKeywordSelected=false
    lateinit var userInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userInfo = getSharedPreferences("userInfo", 0)
        binding.textView14!!.text=intent.getStringExtra("waitingNum").toString()

        binding.editTextPhone1?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phone1= binding.editTextPhone1!!.text.toString()
                if(p0!!.length==4) binding.editTextPhone2?.requestFocus()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.editTextPhone2?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phone2= binding.editTextPhone2!!.text.toString()

            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.editTextPhone3?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phone3= binding.editTextPhone3!!.text.toString()
                if(p0!!.length==3) binding.editTextPhone1?.requestFocus()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        val spinnerData=resources.getStringArray(R.array.spinner_array)
        val adapter=ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerData)
        binding.spinner!!.adapter=adapter
        binding.spinner!!.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->numberOfPeople=2
                    1->numberOfPeople=3
                    2->numberOfPeople=4
                    3->numberOfPeople=5
                    4->numberOfPeople=6
                    5->numberOfPeople=7
                    6->numberOfPeople=8
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val textData: String = binding.textView20!!.text.toString()
        val builder = SpannableStringBuilder(textData)
        builder.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(ForegroundColorSpan(resources.getColor(R.color.INUYellow)), 7, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView20!!.text = builder

        var arr:List<String> =listOf("", "", "")
        val keyword=userInfo.getString("resKeyWord", "").toString()
        for (addr in keyword) {
            val splitedAddr = keyword.split(", ")
            arr = splitedAddr
        }

        var chipList: List<Chip?> = listOf(binding.seatKeyword1,binding.seatKeyword2, binding.seatKeyword3, binding.seatKeyword4, binding.seatKeyword5, binding.seatKeyword6, binding.seatKeyword7, binding.seatKeyword8)
        var n=0
        for(i in arr){
            if(i!=", ") {
                chipList[n]!!.text="#"+i
                chipList[n]!!.visibility=View.VISIBLE
                n+=1
            }
        }


        binding.chipGroup!!.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.seatKeyword1!!.id->searKeyword= binding.seatKeyword1!!.text.toString().replace("#", "")
                binding.seatKeyword2!!.id->searKeyword=binding.seatKeyword2!!.text.toString().replace("#", "")
                binding.seatKeyword3!!.id->searKeyword=binding.seatKeyword3!!.text.toString().replace("#", "")
                binding.seatKeyword4!!.id->searKeyword=binding.seatKeyword4!!.text.toString().replace("#", "")
                binding.seatKeyword5!!.id->searKeyword=binding.seatKeyword5!!.text.toString().replace("#", "")
                binding.seatKeyword6!!.id->searKeyword=binding.seatKeyword6!!.text.toString().replace("#", "")
                binding.seatKeyword7!!.id->searKeyword=binding.seatKeyword7!!.text.toString().replace("#", "")
                binding.seatKeyword8!!.id->searKeyword=binding.seatKeyword8!!.text.toString().replace("#", "")
            }
            isSeatKeywordSelected=true
            binding.checkBox!!.isChecked=false
        }
        binding.checkBox!!.setOnClickListener {
            if(binding.checkBox!!.isChecked) {
                searKeyword=""
                isSeatKeywordSelected=true
            }
            else {
                isSeatKeywordSelected=false
            }
        }
        binding.waitingButton!!.setOnClickListener {

            if(isSeatKeywordSelected){ //조건충족
                dialog1 = CustomDialog(this, "인원: ${numberOfPeople}\n키워드:${searKeyword}\n대기를 신청하시겠습니까?", 0, 0)
                dialog1.isCancelable = false
                dialog1.show(this.supportFragmentManager, "ConfirmDialog")
            }
            else{ //조건 미충족
                dialog1 = CustomDialog(this, "인원과 키워드를 선택해주세요.", 0, 1)
                dialog1.isCancelable = true
                dialog1.show(this.supportFragmentManager, "ConfirmDialog")
            }
        }

        binding.backButton!!.setOnClickListener {
            finish()
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0->{
                addWaiting(AddWaiting( userInfo.getString("resPhNum","").toString(), "${phone3}-${phone1}-${phone2}", numberOfPeople,  searKeyword))
            }
        }
    }
    private fun addWaiting(addWaiting: AddWaiting){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.addWaiting(addWaiting) ?:return
        Log.d("retrofit - hy" ,"${phone3}-${phone1}-${phone2} , ${userInfo.getString("resPhNum","").toString()}, $numberOfPeople,  $searKeyword")
        call.enqueue(object : retrofit2.Callback<WaitingInfo> {

            override fun onResponse(call: Call<WaitingInfo>, response: Response<WaitingInfo>) {
                Log.d("retrofit", "대기 신청 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(response.body()!!.message.contains("한 곳")){
                    val dialog = CustomDialog(this@WaitingActivity, "대기는 한 번에 한 곳만 신청 가능합니다.", 0, 1)
                    dialog.isCancelable = true
                    dialog.show(this@WaitingActivity.supportFragmentManager, "ConfirmDialog")
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1000)
                }
                else if(response.body()!!.message.contains("시간")){
                    val dialog = CustomDialog(this@WaitingActivity, "대기 가능 시간이 아닙니다.", 0, 1)
                    dialog.isCancelable = true
                    dialog.show(this@WaitingActivity.supportFragmentManager, "ConfirmDialog")
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1000)
                }
                else if(response.body()!!.message.contains("완료")){
                    val dialog = CustomDialog(this@WaitingActivity, "대기 신청이 완료되었습니다. ", 0, 1)
                    dialog.isCancelable = true
                    dialog.show(this@WaitingActivity.supportFragmentManager, "ConfirmDialog")
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1000)
                }
                else{
                    val dialog = CustomDialog(this@WaitingActivity, "잠시 후 다시 실행해주세요.", 0, 1)
                    dialog.isCancelable = true
                    dialog.show(this@WaitingActivity.supportFragmentManager, "ConfirmDialog")
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1000)
                }
            }
            override fun onFailure(call: Call<WaitingInfo>, t: Throwable) {
                Log.d("retrofit", "대기 신청 - 응답 실패 / t: $t")
                val dialog = CustomDialog(this@WaitingActivity, "잠시 후 다시 실행해주세요.", 0, 1)
                dialog.isCancelable = true
                dialog.show(this@WaitingActivity.supportFragmentManager, "ConfirmDialog")
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 500)
            }
        })
    }
}


