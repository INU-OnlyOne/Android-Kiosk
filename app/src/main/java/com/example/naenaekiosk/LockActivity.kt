package com.example.naenaekiosk

import android.content.Intent
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.naenaekiosk.databinding.ActivityLockBinding


class LockActivity : AppCompatActivity(), ConfirmDialogInterface{


    private lateinit var binding: ActivityLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pw1.requestFocus()

        val buttonArray= arrayListOf(binding.button0, binding.button1,binding.button2, binding.button3, binding.button4, binding.button5, binding.button6, binding.button7, binding.button8, binding.button9,binding.button10 )
        for(button in buttonArray) button?.setOnClickListener(btnListener)

    }
    private val btnListener = OnClickListener {
        var currentValue=-1
        when(it.id){
            binding.button0.id -> currentValue=0
            binding.button1.id -> currentValue=1
            binding.button2.id -> currentValue=2
            binding.button3.id -> currentValue=3
            binding.button4.id -> currentValue=4
            binding.button5.id -> currentValue=5
            binding.button6.id -> currentValue=6
            binding.button7.id -> currentValue=7
            binding.button8.id -> currentValue=8
            binding.button9.id -> currentValue=9
            binding.imageButton!!.id -> deleteText()
        }
        if(currentValue!=-1){
            when{
                binding.pw1.isFocused -> {
                    binding.pw1.setText(currentValue.toString())
                    binding.pw2.requestFocus()
                }
                binding.pw2.isFocused -> {
                    binding.pw2.setText(currentValue.toString())
                    binding.pw3.requestFocus()
                }
                binding.pw3.isFocused -> {
                    binding.pw3.setText(currentValue.toString())
                    binding.pw4.requestFocus()
                }
                binding.pw4.isFocused -> {
                    binding.pw4.setText(currentValue.toString())
                    confirmPassword()
                }
            }
        }
    }
    private fun confirmPassword(){
        val givenPassword="${binding.pw1.text}${binding.pw2.text}${binding.pw3.text}${binding.pw4.text}"
        //저장된 핀번호
        val savedPIN = this.getSharedPreferences("userInfo", MODE_PRIVATE).getString("userPIN", "0000")
        if(givenPassword==savedPIN){
            val intent= Intent(this, ManageWaitingActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val dialog = CustomDialog(this@LockActivity, "잘못된 번호입니다." , 0, 1)
            dialog.isCancelable = true
            dialog.show(this.supportFragmentManager,"ConfirmDialog")
            clearText()
        }
    }
    private fun clearText(){
        binding.pw1.setText("")
        binding.pw2.setText("")
        binding.pw3.setText("")
        binding.pw4.setText("")
        binding.pw1.requestFocus()
    }
    private fun deleteText(){
        when{
            binding.pw1.isFocused -> {
                binding.pw1.setText("")
            }
            binding.pw2.isFocused -> {
                binding.pw1.setText("")
                binding.pw1.requestFocus()
            }
            binding.pw3.isFocused -> {
                binding.pw2.setText("")
                binding.pw2.requestFocus()
            }
            binding.pw4.isFocused -> {
                binding.pw3.setText("")
                binding.pw3.requestFocus()
            }
        }
    }
    override fun onYesButtonClick(num: Int, theme: Int) {
    }

}
