package com.example.calculator

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.concurrent.TimeUnit
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient


class MainActivity : AppCompatActivity() {

    lateinit var client: ActivityRecognitionClient
    private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".TRANSITIONS_RECEIVER_ACTION"
    /*private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".ActivityTransitionReceiver"*/
    //private val TRANSITIONS_RECEIVER_ACTION = "com.example.TRANSITIONS_RECEIVER_ACTION"


    lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var latitude: String = "latitude"
    var longitude: String = "longitude"
    private val permissionCode = 101

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findLocation()
        binding.apply {
            btnPeriodic.setOnClickListener {
                myPeriodicWork()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun findLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("latitude", location.toString())
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()}
                    showNotification()
                }


            /*fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(listener: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener {
                    if (it == null)
                        Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                    else {
                        latitude = it.latitude.toString()
                        longitude = it.longitude.toString()
                        showNotification()
                    }
                }*/
        }
    }

    private fun myPeriodicWork() {
        val constaints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        client = ActivityRecognition.getClient(this)

        /*val broadcast = ActivityTransitionReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcast,
            IntentFilter("com.example.myapplication.ACTION_PROCESS_ACTIVITY_TRANSITIONS")
        )*/
        requestForUpdates()
        findViewById<Switch>(R.id.switch1).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            else
                Log.d("doWork", "disabled")
        }
    }

    private fun requestForUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            val pendingIntent = getPendingIntent()
            client
                .requestActivityTransitionUpdates(
                    ActivityTransitionUtil.getTransitionRequest(),
                    pendingIntent
                )
                .addOnSuccessListener {
                    Log.d("doWork", "Yes")
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Log.d("doWork", "No")
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }

            /*client.requestActivityUpdates(3000, pendingIntent)
                .addOnSuccessListener {
                    Log.d("doWork", "Yes")
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                }*/
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(TRANSITIONS_RECEIVER_ACTION)
        Log.d("doWork", intent.toString())
        /*val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )*/
       /* val pendingIntent = PendingIntent.getBroadcast(
            this,
            4,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )*/
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                this,
                1200,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        this.registerReceiver(ActivityTransitionReceiver(), IntentFilter(TRANSITIONS_RECEIVER_ACTION))
        Log.d("doWork", TRANSITIONS_RECEIVER_ACTION)
        return pendingIntent
        val myWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constaints)
                .addTag("my_id")
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val notification = Notification.Builder(applicationContext, MyWorker.CHANNEl_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("NewTask")
            .setContentText(latitude + "\n" + longitude)
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "channel name"
            val channelDescription = "channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel =
                NotificationChannel(MyWorker.CHANNEl_ID, channelName, channelImportance).apply {
                    description = channelDescription
                }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(MyWorker.NOTIFICATION, notification.build())
        }
    }
}