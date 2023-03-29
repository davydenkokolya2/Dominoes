package com.example.calculator.recognition

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.calculator.BuildConfig
import com.example.calculator.MainActivity
import com.example.calculator.MyJobIntentService
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient

class ActivityRecognition(private val activity: MainActivity) {
    lateinit var client: ActivityRecognitionClient

    private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".TRANSITIONS_RECEIVER_ACTION"

    @RequiresApi(Build.VERSION_CODES.S)
    fun startActivityRecognition() {
        client = ActivityRecognition.getClient(activity)

        if (ActivityCompat.checkSelfPermission(
                activity,
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
/*Log.d("doWork", "registration receiver " + this.registerReceiver(
            ActivityTransitionReceiver(),
            IntentFilter(TRANSITIONS_RECEIVER_ACTION)
        ).toString()
        )*/
                    Log.d("doWork", "Success")
                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Log.d("doWork", "Error")
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(TRANSITIONS_RECEIVER_ACTION)
        Log.d("doWork", intent.toString())
        /*val pendingIntent = PendingIntent.getService(
            activity,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )*/
        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            4,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        /*val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                activity,
                1200,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(activity, 1200, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }*/



        Log.d(
            "doWork", "airplane receiver " + activity.registerReceiver(
                MyJobIntentService(),
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            ).toString()
        )

        return pendingIntent
    }
}