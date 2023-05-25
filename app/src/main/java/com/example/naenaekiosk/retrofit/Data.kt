package com.example.naenaekiosk.retrofit

import java.io.Serializable

data class AddWaiting(
    val resPhNum : String,
    val UserPhone : String,
    val WaitHeadcount : Int,
    val WaitSeat : String,
):Serializable

data class WaitingInfo(
    val message:String,
    val waitTime : String
):Serializable

data class resPhNum(
    val resPhNum:String
)

data class AcceptWaiting(
    val waitIndex:String,
    val resPhNum:String,
    val userPhone:String
)

data class message(
    val message:String
)

data class waitIndex(
    val waitIndex:String
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
    val WaitSeat:String,
    val WaitisAccepted:Int
)

data class WaitList(
    val result:List<WaitListItem>
)

data class StartWaiting(
    val resPhNum:String,
    val startTime:String
)


data class EndWaiting(
    val resPhNum:String,
    val endTime:String
)
