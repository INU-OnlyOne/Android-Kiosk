package com.example.naenaekiosk

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.naenaekiosk.databinding.ActivityLockBinding


class LockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pw1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length >= 1) {
                    binding.pw2.requestFocus()
                    //binding.pw1.backgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this@LockActivity, R.color.INUYellow)))
                }
            }
            override fun afterTextChanged(editable: Editable) {
            }
        })
    }
}