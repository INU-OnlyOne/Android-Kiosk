package com.example.naenaekiosk.retrofit

import com.example.naenaekiosk.*
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @POST("/user/waiting/insert")
    fun addWaiting(@Body addWaiting: AddWaiting):Call<AddWaiting>

    @POST("/kiosk/accept")
    fun acceptWaiting(@Body AcceptWaiting: AcceptWaiting):Call<String>

    @POST("/kiosk/reject")
    fun rejectWaiting(@Body RejectWaiting: RejectWaiting):Call<String>

    @POST("/kiosk/waitedList")
    fun callList(@Body resPhNum: resPhNum):Call<CallList>

    @POST("/kiosk/waitinginfo")
    fun waitingList(@Body resPhNum: resPhNum):Call<WaitList>

}
