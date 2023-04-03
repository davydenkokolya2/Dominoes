package com.example.calculator.remote.model

import java.sql.Time
import java.time.LocalTime

data class Geolocation(
    val latitude: Double,
    val longitude: Double,
    val sos: Boolean,
    val userId: Int = 2,
    val time: String,
    )
