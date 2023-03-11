package com.example.naenaekiosk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.naenaekiosk.databinding.ActivityMainBinding
import com.example.naenaekiosk.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idWarning!!.visibility= View.INVISIBLE
        binding.inputId?.setOnFocusChangeListener { view, b ->
            when(b){

                false -> {
                    //todo 아이디 중복 확인
                    binding.idWarning!!.visibility= View.VISIBLE
                }
            }
        }
        binding.pwWarning!!.visibility=View.INVISIBLE
        binding.inputPW2?.addTextChangedListener (object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inputPW?.text.toString() == binding.inputPW2?.text.toString())binding.pwWarning!!.visibility=View.INVISIBLE
                else binding.pwWarning!!.visibility=View.VISIBLE
            }
        })
    }
}