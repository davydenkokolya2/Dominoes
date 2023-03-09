package com.example.calculator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.ActivityTransitionResult


class ActivityTransitionReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("doWork", "BroadcastReceiver")
        Log.d("doWork", intent.toString())
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent!!)
            result?.let {
                result.transitionEvents.forEach { event ->
                    val info =
                        "Transition ${ActivityTransitionUtil.toActivityString(event.activityType)} - ${
                            ActivityTransitionUtil.toTransitionType(event.transitionType)
                        }"
                    /*val channel = NotificationChannel(
                        "id", "name", NotificationManager.IMPORTANCE_HIGH
                    )
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel, )
                    val builder = NotificationCompat.Builder(this, 101)

                        .setContentTitle("Напоминание")
                        .setContentText("Пора покормить кота")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify("channelID", builder.build())*/

                    Toast.makeText(context, info, Toast.LENGTH_LONG).show()
                    Log.d("doWork", info)
                }
            }

        }
    }
}