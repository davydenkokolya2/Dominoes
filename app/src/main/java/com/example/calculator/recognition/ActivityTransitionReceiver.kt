package com.example.calculator.recognition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.calculator.service.ForegroundSOSService
import com.google.android.gms.location.ActivityTransitionResult


class ActivityTransitionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentForeground = Intent(context, ForegroundSOSService::class.java)
        val result = ActivityTransitionResult.extractResult(intent!!)
        result?.let {
            result.transitionEvents.forEach { event ->
                val info =
                    "Transition ${ActivityTransitionUtil.toActivityString(event.activityType)} - ${
                        ActivityTransitionUtil.toTransitionType(event.transitionType)
                    }"
                if (event.transitionType == 0 && event.activityType == 0)
                    context?.startForegroundService(intentForeground)
                else
                    context?.stopService(intentForeground)

                Toast.makeText(context, info, Toast.LENGTH_LONG).show()
                Log.d("doWork", info)
            }
        }
    }
}