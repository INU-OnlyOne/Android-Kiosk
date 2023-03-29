package com.example.naenaekiosk

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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.naenaekiosk.databinding.ActivityWaitingBinding

class WaitingActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityWaitingBinding
    private lateinit var dialog1:CustomDialog
    private var phone1="0000"
    private var phone2="0000"
    private var phone3="010"

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
        var numberOfPeople=2
        var spinnerData=resources.getStringArray(R.array.spinner_array)
        var adapter=ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerData)
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
        binding.waitingButton!!.setOnClickListener {
            // todo 조건 확인 코드 필요
            /*
            dialog1 = CustomDialog(this, "대기를 신청하시겠습니까?", 0, 0)
            dialog1.isCancelable = false
            dialog1.show(this.supportFragmentManager, "ConfirmDialog")

             */
        }

        binding.backButton!!.setOnClickListener {
            finish()
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0->{
                //todo 대기 신청 연결
                dialog1.dismiss()
                // todo 작성 완료 팝업. 나중에 레트로핏 안으로 옮기기
                val dialog = CustomDialog(this, "대기 신청이 완료되었습니다.", 0, 1)
                dialog.isCancelable = true
                dialog.show(this.supportFragmentManager, "ConfirmDialog")
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 500)
            }
        }
    }
}