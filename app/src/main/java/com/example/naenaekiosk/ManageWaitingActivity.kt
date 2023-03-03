package com.example.naenaekiosk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.naenaekiosk.databinding.ActivityManageWaitingBinding

class ManageWaitingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageWaitingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityManageWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}