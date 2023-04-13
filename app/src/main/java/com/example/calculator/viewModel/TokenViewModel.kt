package com.example.calculator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.remote.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object TokenViewModel: ViewModel() {
    private val _stateToken = MutableStateFlow<Token?>(null)
    val stateToken: StateFlow<Token?> = _stateToken

    fun loadToken(token: Token) {
        //Log.d(TAG, token.firstName)
        viewModelScope.launch(Dispatchers.IO) {
            _stateToken.emit(token)
        }
    }
}
