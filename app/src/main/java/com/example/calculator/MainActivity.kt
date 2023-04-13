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
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.recognition.ActivityRecognition
import com.example.calculator.service.ForegroundService
import com.example.calculator.viewModel.TokenViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val tokenViewModel = TokenViewModel
    //val geolocationViewModel = GeolocationViewModel()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*lifecycleScope.launch {
            tokenViewModel.stateToken.collect {
                if (it != null)
                    (binding.fragmentContainer as NavHostFragment).navController.navInflater.inflate(
                        R.navigation.main_graph
                    ).setStartDestination(R.id.userProfileFragment)
            }
        }*/
        //(binding.fragmentContainer as NavHostFragment).navController.navInflater.inflate(R.navigation.main_graph).setStartDestination(R.id.userProfileFragment)

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