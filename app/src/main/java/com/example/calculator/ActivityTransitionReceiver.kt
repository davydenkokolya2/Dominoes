package com.example.calculator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.ActivityTransitionResult


class ActivityTransitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("doWork", "BroadcastReceiver")
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent!!)
            result?.let {
                result.transitionEvents.forEach { event ->
                    val info =
                        "Transition ${ActivityTransitionUtil.toActivityString(event.activityType)} - ${
                            ActivityTransitionUtil.toTransitionType(event.transitionType)
                        }"
                    Toast.makeText(context, info, Toast.LENGTH_LONG).show()
                    Log.d("doWork", info)
                }
            }

        }
    }
}