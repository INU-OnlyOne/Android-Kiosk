package com.example.naenaekiosk.retrofit

import com.example.naenaekiosk.*
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @POST("/restaurant/waiting")
    fun addWaiting(@Body addWaiting: AddWaiting):Call<WaitingInfo>

    @POST("/kiosk/accept") //손님 호출
    fun acceptWaiting(@Body AcceptWaiting: AcceptWaiting):Call<message>

    @POST("/kiosk/reject") //타임아웃
    fun rejectWaiting(@Body waitIndex: waitIndex):Call<message>

    @POST("/kiosk/enter") //입장 완료
    fun enter(@Body waitIndex: waitIndex):Call<message>

    @POST("/kiosk/waitedList")
    fun callList(@Body resPhNum: resPhNum):Call<CallList>

    @POST("/kiosk/waitinginfo")
    fun waitingList(@Body resPhNum: resPhNum):Call<WaitList>

    @POST("/kiosk/waiting/start")
    fun startWaiting(@Body StartWaiting: StartWaiting):Call<StartWaiting>

    @POST("/kiosk/waiting/end")
    fun endWaiting(@Body EndWaiting: EndWaiting):Call<EndWaiting>
}
