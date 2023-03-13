package com.example.naenaekiosk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naenaekiosk.databinding.ActivityWaitingBinding

class WaitingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaitingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}