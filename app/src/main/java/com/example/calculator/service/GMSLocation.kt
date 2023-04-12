package com.example.calculator.service

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.calculator.remote.OkHttp.Okhttp
import com.example.calculator.remote.model.Geolocation
import com.example.calculator.viewModel.TokenViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class GMSLocation(private val service: ForegroundSOSService, private val okHttpClient: Okhttp) {

    //val okHttpClient = Okhttp(service)
    fun findLocation(sos: Boolean) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(service)

        if (ActivityCompat.checkSelfPermission(
                service,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                service,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Log.d("doWork", "first")
            /*return locationDTO*/
        } else {
            //Log.d("doWork", "second")
            /*Log.d("doWork",
                fusedLocationClient.lastLocation.addOnSuccessListener{Log.d("doWork", it.longitude.toString())}
                    .toString()
            )*/

            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(listener: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener {
                    if (it != null) {
                        Log.d("doWork", "inListener " + it.toString())
                        /*Toast.makeText(
                            service,
                            it.longitude.toString() + " " + it.latitude.toString(),
                            Toast.LENGTH_LONG
                        ).show()*/
                        val latitude = it.latitude
                        val longitude = it.longitude
                        CoroutineScope(Dispatchers.IO).launch {
                            TokenViewModel.stateUserId.collect {
                                okHttpClient.sendGeolocation(
                                    Geolocation(
                                        latitude,
                                        longitude,
                                        sos,
                                        time = LocalTime.now().toString(),
                                        date = LocalDate.now().toString(),
                                        userId = it!!.userId
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }
}
