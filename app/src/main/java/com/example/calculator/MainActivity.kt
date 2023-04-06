package com.example.calculator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculator.DTO.UserDTO
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.recognition.ActivityRecognition
import com.example.calculator.remote.OkHttp.Okhttp
import com.example.calculator.service.ForegroundService
import com.example.calculator.viewModel.GeolocationViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    //val geolocationViewModel = GeolocationViewModel()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                GeolocationViewModel.stateGeolocation.collect {
                    if (it != null) {
                        binding.textView4.text = it.values[0].toString()
                        binding.textView5.text = it.values[1].toString()
                        binding.textView6.text = it.values[2].toString()
                    }
                }

            }
        }
        binding.btnRegistration.setOnClickListener {
            val okhttp = Okhttp()
            okhttp.registration(
                UserDTO(
                    binding.etFirstName.text.toString(),
                    binding.etLastName.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            10
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Yes") { _, _ ->
                    // this request will take user to Application's Setting page
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        100
                    )
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()


        val activityRecognition = ActivityRecognition(this)
        activityRecognition.startActivityRecognition()

        val intent = Intent(this, ForegroundService::class.java)
        startForegroundService(intent)
    }
}