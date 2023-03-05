package com.example.calculator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class DetectedActivityReceiver : BroadcastReceiver(){
    val RECEIVER_ACTION = BuildConfig.APPLICATION_ID + ".DetectedActivityReceiver"

    override fun onReceive(context: Context?, intent: Intent) {

        if (RECEIVER_ACTION == intent.action) {
            Log.d("DetectedActivityReceiver", "Received an unsupported action.")
            return
        }

        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            for (event in result!!.transitionEvents) {
                val activity = activityType(event.activityType).toString()
                val transition = transitionType(event.transitionType).toString()
                val message = "Transition: $activity ($transition)"
                Log.d("DetectedActivityReceiver", message)
            }
        }
    }

    private fun transitionType(transitionType: Int): String? {
        return when (transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }
    }

    private fun activityType(activity: Int): String? {
        return when (activity) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.WALKING -> "WALKING"
            else -> "UNKNOWN"
        }
    }

    val intent = Intent(DetectedActivityReceiver.RECEIVER_ACTION)
    pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

// creating the receiver
    receiver = DetectedActivityReceiver()

// registring the receiver
    LocalBroadcastManager.getInstance(this).registerReceiver(
    receiver, IntentFilter(DetectedActivityReceiverRECEIVER_ACTION)
    )

    val task = ActivityRecognition.getClient(this)
        .requestActivityTransitionUpdates(finalRequest, pendingIntent)

    task.addOnSuccessListener {
        Log.d("ActivityRecognition", "Transitions Api registered with success")
    }

    task.addOnFailureListener { e: Exception ->
        Log.d("ActivityRecognition", "Transitions Api could NOT be registered ${e.localizedMessage}")
    }
}