package com.example.calculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.recognition.ActivityRecognition
import com.example.calculator.service.ForegroundService

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityRecognition = ActivityRecognition(this)
        activityRecognition.startActivityRecognition()

        val intent = Intent(this, ForegroundService::class.java)
        startForegroundService(intent)
    }
}