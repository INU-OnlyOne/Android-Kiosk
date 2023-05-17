package com.example.naenaekiosk.retrofit

import java.io.Serializable

data class AddWaiting(
    val UserPhone : String,
    val resPhNum : String,
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

data class resPhNum(
    val resPhNum:String
)

data class AcceptWaiting(
    val waitIndex:String,
    val resPhNum:String,
    val userPhone:String
)

data class RejectWaiting(
    val resIdx:String,
    val userPhone:String
)

data class CallListItem(
    val WaitedIdx:Int,
    val UserPhone:String,
    val WaitHeadcount:Int,
    val WaitSeat:String,
    val acceptedTime:String
)
data class CallList(
    val code:String,
    val message:List<CallListItem>
)

data class WaitListItem(
    val WaitIndex:Int,
    val UserPhone:String,
    val WaitHeadcount:Int,
    val keyword:String,
    val WaitisAccepted:Int
)

data class WaitList(
    val result:List<WaitListItem>
)