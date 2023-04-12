package com.example.calculator.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculator.databinding.FragmentUserProfileBinding
import com.example.calculator.viewModel.GeolocationViewModel
import com.example.calculator.viewModel.TokenViewModel
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                GeolocationViewModel.stateGeolocation.collect {
                    if (it != null) {
                        binding.textView4.text = it.values[0].toString()
                        binding.textView5.text = it.values[1].toString()
                        binding.textView6.text = it.values[2].toString()
                    }
                }

            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                TokenViewModel.stateUserId.collect {
                    if (it != null) {
                        binding.userName.text = it.firstName
                    }
                }
            }
        }
        return binding.root
    }
}