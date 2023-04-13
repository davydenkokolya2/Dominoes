package com.example.calculator.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.calculator.utils.Constants
import com.example.calculator.viewModel.ErrorViewModel
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            ErrorViewModel.stateError.collect {
                Log.d(Constants.TAG, "изменения error view model")
                if (it != null)
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}