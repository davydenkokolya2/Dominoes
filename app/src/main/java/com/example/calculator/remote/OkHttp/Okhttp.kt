package com.example.calculator.remote.OkHttp

import android.util.Log
import com.example.calculator.utils.Constants.TAG
import com.example.calculator.remote.common.Common.BASE_URL
import com.example.calculator.remote.model.Geolocation
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources

class Okhttp() {
    val gson = Gson()
    val JSON = "application/json; charset=utf-8".toMediaType()
    val client = OkHttpClient()


    fun sendGeolocation(geolocation: Geolocation) {
        val jsonRequest = gson.toJson(geolocation)
        val body: RequestBody = jsonRequest.toRequestBody(JSON)
        val request = Request.Builder().url(BASE_URL + "/geolocation").post(body).build()
        try {
            client.newCall(request).enqueue(responseCallback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("doWork", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {

                }
            })
        } catch (e: IOException) {
            Log.d("doWork", "Ошибка подключения: $e")
            println("Ошибка подключения: $e")
        }
    }

    val eventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)
            Log.d(TAG, "Connection Opened")
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Log.d(TAG, "Connection Closed")
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            super.onEvent(eventSource, id, type, data)
            Log.d(TAG, "On Event Received! Data -: $data")
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)
            Log.d(TAG, "On Failure -: ${response?.body}")
        }
    }

    val okhttpClient = OkHttpClient.Builder()/*.connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)*/
        .build()

    val request = Request.Builder()
        .url(BASE_URL + "/open-sse-stream/nickk")
        //.header("Accept", "application/json; q=0.5")
        //.addHeader("Accept", "text/event-stream")
        .build()

    private lateinit var eventSource: EventSource

    fun listenServer() {
        eventSource = EventSources.createFactory(okhttpClient)
            .newEventSource(request = request, listener = eventSourceListener)
        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).enqueue(responseCallback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "API Call Failure ${e.localizedMessage}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d(TAG, "APi Call Success ${response.body.toString()}")
                }
            })
        }
    }

    fun closeConnection() {
        eventSourceListener.onClosed(eventSource)
    }
}
