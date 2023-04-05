package com.example.calculator.service

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
import com.example.calculator.DTO.LocationDTO
import com.example.calculator.R
import com.example.calculator.remote.OkHttp.Okhttp
import com.example.calculator.utils.Constants
import com.example.calculator.viewModel.GeolocationViewModel
import java.util.*

class ForegroundSOSService() : Service(), SensorEventListener {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private lateinit var locationDTO: LocationDTO
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private val okhttp = Okhttp(this)
    private val gmsLocation = GMSLocation(this, okhttp)
    private val timer = Timer()
    //val geolocationViewModel = GeolocationViewModel()


    override fun onCreate() {
        super.onCreate()
        Log.d("doWork", "start SOS foreground")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_2"
            val channel =
                NotificationChannel(channelId, "default", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val notification = Notification.Builder(this, channelId).apply {
                setContentTitle("ForegroundSOSService")
                setContentText("ForegroundSOSService")
                setSmallIcon(R.drawable.ic_launcher_foreground)
            }.build()

            startForeground(Constants.SERVICE_ID, notification)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)


        okhttp.listenServer()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                gmsLocation.findLocation(false)
            }
        }, 0, 120000)
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onSensorChanged(event: SensorEvent?) {


        if (event != null) {
            GeolocationViewModel.loadGeolocation(event)
            if (Math.abs(event.values[0]) > 20 || Math.abs(event.values[1]) > 20 || Math.abs(event.values[2]) > 20) {
                Log.d("doWork", "SOS")
                //gmsLocationViewModel.findLocation()
                gmsLocation.findLocation(true)

                if (this::locationDTO.isInitialized)
                    Log.d("doWork", locationDTO.toString())
                //Log.d("doWork", locationDTO.longitude.toString() + " " + locationDTO.latitude.toString())*/
                /*Toast.makeText(
                    this,
                    locationDTO.longitude.toString() + " " + locationDTO.latitude.toString(),
                    Toast.LENGTH_LONG
                ).show()*/
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        okhttp.closeConnection()
        timer.cancel()
        Log.d("doWork", "kill SOS service")
        Toast.makeText(this, "kill SOS service", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
}