package com.example.calculator

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.calculator.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    /*private val TRANSITIONS_RECEIVER_ACTION =
        BuildConfig.APPLICATION_ID + ".ActivityTransitionReceiver"*/
    //private val TRANSITIONS_RECEIVER_ACTION = "com.example.TRANSITIONS_RECEIVER_ACTION"

    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPeriodic.setOnClickListener {
                myPeriodicWork()
            }
        }
        val activityRecognition = ActivityRecognition(this)
        activityRecognition.startActivityRecognition()
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