package com.example.calculator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.remote.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object TokenViewModel: ViewModel() {
    private val _stateUserId = MutableStateFlow<Token?>(null)
    val stateUserId: StateFlow<Token?> = _stateUserId

    fun loadUserId(token: Token) {
        //Log.d(TAG, token.firstName)
        viewModelScope.launch(Dispatchers.IO) {
            _stateUserId.emit(token)
        }
    }
}
