package com.example.calculator.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.calculator.Constants.SERVICE_ID
import com.example.calculator.R


class ForegroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("doWork", "start foreground")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_1"
            val channel =
                NotificationChannel(channelId, "default", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val notification = Notification.Builder(this, channelId).apply {
                setContentTitle("notification title")
                setContentText("ForegroundService")
                setSmallIcon(R.drawable.ic_launcher_foreground)
            }.build()

            startForeground(SERVICE_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*Timer().scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                Log.d("doWork", "foreground service run")
            }
        }, 0, 5000)*/

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("doWork", "kill service")
        Toast.makeText(this, "kill service", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
}