package com.example.calculator.remote.retrofit

import com.example.calculator.remote.model.Geolocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {
    @POST("geolocation")
    fun sendGeolocation(@Body geolocation: Geolocation): Call<Boolean>

    @GET("/loginn/")
    fun getAccounts(): Call<Boolean>

    /*@GET("api/loginn")
    suspend fun getItunesAlbums(): Boolean*/
}