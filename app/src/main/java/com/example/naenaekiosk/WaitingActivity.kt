package com.example.naenaekiosk

import android.graphics.Typeface
import android.os.Build
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
import androidx.annotation.RequiresApi
import com.example.naenaekiosk.databinding.ActivityWaitingBinding
import com.example.naenaekiosk.retrofit.API
import com.example.naenaekiosk.retrofit.AddWaiting
import com.example.naenaekiosk.retrofit.IRetrofit
import com.example.naenaekiosk.retrofit.RetrofitClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private var resId="000-000-0000" //todo resid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        binding.chipGroup!!.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.seatKeyword1!!.id->searKeyword= binding.seatKeyword1!!.text.toString()
                binding.seatKeyword2!!.id->searKeyword=binding.seatKeyword2!!.text.toString()
                binding.seatKeyword3!!.id->searKeyword=binding.seatKeyword3!!.text.toString()
                binding.seatKeyword4!!.id->searKeyword=binding.seatKeyword4!!.text.toString()
                binding.seatKeyword5!!.id->searKeyword=binding.seatKeyword5!!.text.toString()
                binding.seatKeyword6!!.id->searKeyword=binding.seatKeyword6!!.text.toString()
                binding.seatKeyword7!!.id->searKeyword=binding.seatKeyword7!!.text.toString()
                binding.seatKeyword8!!.id->searKeyword=binding.seatKeyword8!!.text.toString()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0->{
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val date = current.format(formatter)
                addWaiting(AddWaiting("${phone3}-${phone1}-${phone2}" , resId, numberOfPeople, date, searKeyword, false))
                dialog1.dismiss()
            }
        }
    }
    private fun addWaiting(addWaiting: AddWaiting){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.addWaiting(addWaiting = addWaiting) ?:return

        call.enqueue(object : retrofit2.Callback<AddWaiting> {

            override fun onResponse(call: Call<AddWaiting>, response: Response<AddWaiting>) {
                Log.d("hy", addWaiting.toString())
                Log.d("retrofit", "대기 신청 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val dialog = CustomDialog(this@WaitingActivity, "대기 신청이 완료되었습니다.", 0, 1)
                dialog.isCancelable = true
                dialog.show(this@WaitingActivity.supportFragmentManager, "ConfirmDialog")
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 500)
            }
            override fun onFailure(call: Call<AddWaiting>, t: Throwable) {
                Log.d("retrofit", "대기 신청 - 한식 응답 실패 / t: $t")
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


