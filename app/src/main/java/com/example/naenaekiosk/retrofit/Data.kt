package com.example.naenaekiosk.retrofit

import java.io.Serializable

data class AddWaiting(
    val userID : String,
    val ResID : String,
    val Waitheadcount : Int,
    val WaitTime : String,
    val WaitSeat : String,
    val WaitisAccepted : Boolean
): Serializable

data class Customer(
    val waitingNum:Int,
    val userPhone:String,
    val headCount:Int,
    val keyword:String,
    val callTime:String
)
