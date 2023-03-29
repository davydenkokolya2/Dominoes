package com.example.calculator

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class ForegroundService : Service(), SensorEventListener {

    private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".TRANSITIONS_RECEIVER_ACTION"
    val SERVICE_ID = 1
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

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
                setContentText("this is content")
                setSmallIcon(R.drawable.ic_launcher_foreground)
            }.build()

            startForeground(SERVICE_ID, notification)
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("doWork", "hello")
        //Handler(Looper.getMainLooper()).postDelayed({
        Toast.makeText(this, "hello world", Toast.LENGTH_LONG).show()
        //}, 3000)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            Log.d("doWork", "X " + event.values[0].toString())
            Log.d("doWork", "Y " + event.values[1].toString())
            Log.d("doWork", "Z " + event.values[2].toString())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        Log.d("doWork", "kill service")
        Toast.makeText(this, "kill service", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
}