package com.example.calculator

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient


class MainActivity : AppCompatActivity() {

    lateinit var client: ActivityRecognitionClient
    private val TRANSITIONS_RECEIVER_ACTION = BuildConfig.APPLICATION_ID + ".ActivityTransitionReceiver"
    //private val TRANSITIONS_RECEIVER_ACTION = "com.example.TRANSITIONS_RECEIVER_ACTION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            client
                .requestActivityTransitionUpdates(
                    ActivityTransitionUtil.getTransitionRequest(),
                    getPendingIntent()
                )
                //.requestActivityUpdates(1000, getPendingIntent())
                .addOnSuccessListener {
                    Log.d("doWork", "Yes")
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Log.d("doWork", "No")
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(TRANSITIONS_RECEIVER_ACTION)
        Log.d("doWork", intent.toString())
        val pendingIntent =  PendingIntent.getBroadcast(
            this,
            255,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        this.registerReceiver(ActivityTransitionReceiver(), IntentFilter(TRANSITIONS_RECEIVER_ACTION))
        //Log.d("doWork", pendingIntent.creatorPackage.toString())
        return pendingIntent
    }
}