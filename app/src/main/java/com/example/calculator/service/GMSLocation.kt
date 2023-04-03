package com.example.calculator.service

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.calculator.remote.common.Common
import com.example.calculator.remote.model.Geolocation
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class GMSLocation(private val service: ForegroundSOSService) {
    private val mService = Common.retrofitService
    fun findLocation() {

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
            Log.d("doWork", "first")
            /*return locationDTO*/
        } else {
            Log.d("doWork", "second")
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
                        Toast.makeText(
                            service,
                            it.longitude.toString() + " " + it.latitude.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                        mService.sendGeolocation(Geolocation(it.latitude, it.longitude, true, time = LocalDateTime.now().toString())).enqueue(object : Callback<Boolean> {
                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                Log.d("doWork", t.toString())
                            }

                            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                Log.d("doWork", response.toString() + call.toString())
                            }
                        })

                    }
                }
        }
    }
}