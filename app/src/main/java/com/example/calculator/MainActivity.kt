package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.recognition.ActivityRecognition
import com.example.calculator.remote.common.Common
import com.example.calculator.service.ForegroundService
import com.google.android.gms.common.internal.safeparcel.SafeParcelableSerializer
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    /*private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".ActivityTransitionReceiver"*/
    //private val TRANSITIONS_RECEIVER_ACTION = "com.example.TRANSITIONS_RECEIVER_ACTION"
    private val mService = Common.retrofitService
    lateinit var binding: ActivityMainBinding
    private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".TRANSITIONS_RECEIVER_ACTION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*binding.apply {
            btnPeriodic.setOnClickListener {
                myPeriodicWork()
            }
        }*/

        val activityRecognition = ActivityRecognition(this)
        activityRecognition.startActivityRecognition()

        val intent = Intent(this, ForegroundService::class.java)
        binding.btnPeriodic.setOnClickListener {
            Log.d("doWork", stopService(intent).toString())
        }
        startForegroundService(intent)
        mService.getAccounts().enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d("doWork", t.toString())
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                Log.d("doWork", response.toString())
            }
        })
        //val intent = Intent(this, ActivityTransitionReceiver::class.java)

        intent.action = BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION"
        val events: ArrayList<ActivityTransitionEvent> = arrayListOf()

        // You can set desired events with their corresponding state
        val transitionEventStart = ActivityTransitionEvent(
            DetectedActivity.IN_VEHICLE,
            ActivityTransition.ACTIVITY_TRANSITION_ENTER,
            SystemClock.elapsedRealtimeNanos()
        )
        val transitionEventStop = ActivityTransitionEvent(
            DetectedActivity.IN_VEHICLE,
            ActivityTransition.ACTIVITY_TRANSITION_EXIT,
            SystemClock.elapsedRealtimeNanos()
        )
        binding.btnStart.setOnClickListener {
            events.add(transitionEventStart)
            val result = ActivityTransitionResult(events)
            SafeParcelableSerializer.serializeToIntentExtra(
                result,
                intent,
                "com.google.android.location.internal.EXTRA_ACTIVITY_TRANSITION_RESULT"
            )
            this.sendBroadcast(intent)
        }
        binding.btnStop.setOnClickListener {
            events.add(transitionEventStop)
            val result = ActivityTransitionResult(events)
            SafeParcelableSerializer.serializeToIntentExtra(
                result,
                intent,
                "com.google.android.location.internal.EXTRA_ACTIVITY_TRANSITION_RESULT"
            )
            this.sendBroadcast(intent)
        }

    }

    private fun myPeriodicWork() {
        val constaints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        /*val broadcast = ActivityTransitionReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcast,
            IntentFilter("com.example.myapplication.ACTION_PROCESS_ACTIVITY_TRANSITIONS")
        )*/

        val myWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constaints)
                .addTag("my_id")
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest)
    }
}