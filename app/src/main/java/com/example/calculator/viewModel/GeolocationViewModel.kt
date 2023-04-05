package com.example.calculator.viewModel

import android.hardware.SensorEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object GeolocationViewModel : ViewModel() {
    private val _stateGeolocation = MutableSharedFlow<SensorEvent?>()
    val stateGeolocation: SharedFlow<SensorEvent?> = _stateGeolocation

    fun loadGeolocation(event: SensorEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateGeolocation.emit(event)
        }
    }
}