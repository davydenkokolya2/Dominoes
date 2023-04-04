package com.example.calculator.remote.model

data class Geolocation(
    val latitude: Double,
    val longitude: Double,
    val sos: Boolean,
    val userId: Int = 2,
    val time: String,
    val date: String
    )
