package com.example.naenaekiosk.retrofit

import com.example.naenaekiosk.*
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    @POST("/user/waiting/insert")
    fun addWaiting(@Body addWaiting: AddWaiting):Call<AddWaiting>

}
