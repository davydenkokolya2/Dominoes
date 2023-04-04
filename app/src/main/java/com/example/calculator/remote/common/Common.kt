package com.example.calculator.remote.common

import com.example.calculator.remote.retrofit.RetrofitClient
import com.example.calculator.remote.retrofit.RetrofitService

object Common {
    val BASE_URL = "https://6abf-2a02-d247-1102-2b1a-3c11-78-41ed-ad0c.eu.ngrok.io"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}