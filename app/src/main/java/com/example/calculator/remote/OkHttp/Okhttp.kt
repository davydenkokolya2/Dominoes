package com.example.calculator.remote.OkHttp

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.calculator.DTO.UserDTO
import com.example.calculator.R
import com.example.calculator.remote.common.Common.BASE_URL
import com.example.calculator.remote.model.Geolocation
import com.example.calculator.remote.model.Token
import com.example.calculator.service.ForegroundSOSService
import com.example.calculator.utils.Constants.SERVICE_ID
import com.example.calculator.utils.Constants.TAG
import com.example.calculator.viewModel.TokenViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.io.IOException
import java.util.concurrent.TimeUnit

class Okhttp() {

    lateinit var service: ForegroundSOSService

    constructor(service: ForegroundSOSService) : this() {
        this.service = service
    }

    val gson = GsonBuilder().create()
    val JSON = "application/json; charset=utf-8".toMediaType()

    val okhttpClient = OkHttpClient.Builder()
        // .protocols(Collections.singletonList(Protocol.HTTP_1_1))
        .connectionPool(ConnectionPool(1, 20, TimeUnit.MINUTES))
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .build()

    fun registration(userDTO: UserDTO) {
        val jsonRequest = gson.toJson(userDTO)
        val body: RequestBody = jsonRequest.toRequestBody(JSON)
        val request = Request.Builder().url(BASE_URL + "/authenticate").post(body).build()

        try {
            okhttpClient.newCall(request).enqueue(responseCallback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("doWork", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    TokenViewModel.loadUserId(
                        gson.fromJson(
                            response.body!!.string(),
                            Token::class.java
                        )
                    )
                }
            })
        } catch (e: IOException) {
            Log.d("doWork", "Ошибка подключения: $e")
            println("Ошибка подключения: $e")
        }
    }

    fun sendGeolocation(geolocation: Geolocation) {
        val jsonRequest = gson.toJson(geolocation)
        val body: RequestBody = jsonRequest.toRequestBody(JSON)

        CoroutineScope(Dispatchers.IO).launch {
            TokenViewModel.stateUserId.collect {
                val request = Request.Builder().url(BASE_URL + "/geolocation").post(body)
                    .addHeader("Authorization", "Bearer " + it!!.accessToken)
                    .build()
                try {
                    okhttpClient.newCall(request).enqueue(responseCallback = object : Callback {
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
        }
    }


    val eventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)
            Log.d(TAG, "Connection Opened")
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Log.d(TAG, "Connection Closed ${eventSource.request().isHttps}")
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            super.onEvent(eventSource, id, type, data)
            Log.d(TAG, "On Event Received! Data -: $data")

            val fullScreenIntent = Intent(service, ForegroundSOSService::class.java)
            val fullScreenPendingIntent = PendingIntent.getActivity(
                service, 0,
                fullScreenIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder =
                NotificationCompat.Builder(service, "my_channel_2")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Incoming call")
                    .setContentText("(919) 555-1234")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)

                    // Use a full-screen intent only for the highest-priority alerts where you
                    // have an associated activity that you would like to launch after the user
                    // interacts with the notification. Also, if your app targets Android 10
                    // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                    // order for the platform to invoke this notification.
                    .setFullScreenIntent(fullScreenPendingIntent, true)

            val incomingCallNotification = notificationBuilder.build()
            if (data == "{\"message\":\"sos\"}")
                service.startForeground(SERVICE_ID, incomingCallNotification)
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)
            Log.d(TAG, "On Failure -: ${response?.body}")
            Log.d(
                TAG,
                "On Failure -: ${t?.localizedMessage} ${t?.message} ${eventSource.request().headers}"
            )
        }
    }

    private lateinit var eventSource: EventSource

    fun listenServer() {
        CoroutineScope(Dispatchers.IO).launch {
            TokenViewModel.stateUserId.collect {
                val request = Request.Builder()
                    .url(BASE_URL + "/open-sse-stream/${it?.userId}")
                    .addHeader("Authorization", "Bearer " + it!!.accessToken)
                    /*.header("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "text/event-stream")*/
                    .build()
                eventSource = EventSources.createFactory(okhttpClient)
                    .newEventSource(request = request, listener = eventSourceListener)

                okhttpClient.newCall(request).enqueue(responseCallback = object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "API Call Failure ${e.localizedMessage}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.d(TAG, "APi Call Success ${response.body.toString()}")
                        /*Log.d(
                            TAG,
                            "connection count " + okhttpClient.connectionPool.connectionCount()
                                .toString()
                        )*/
                    }
                })
            }
        }
    }

    fun closeConnection() {
        eventSource.cancel()
        //okhttpClient.dispatcher.executorService.shutdown();
    }
}
