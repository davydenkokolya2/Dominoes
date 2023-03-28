package com.example.calculator

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient

class ActivityRecognition(private val activity: MainActivity) {
    lateinit var client: ActivityRecognitionClient

    private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".TRANSITIONS_RECEIVER_ACTION"

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
                    Log.d("doWork", "Yes")
                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Log.d("doWork", "No")
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                }
        }
    }

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
                activity,
                1200,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(activity, 1200, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        activity.registerReceiver(
            ActivityTransitionReceiver(),
            IntentFilter(TRANSITIONS_RECEIVER_ACTION)
        )

        //Log.d("doWork", TRANSITIONS_RECEIVER_ACTION)

        return pendingIntent
    }
}