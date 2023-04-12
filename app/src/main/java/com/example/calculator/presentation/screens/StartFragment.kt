package com.example.calculator.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculator.R
import com.example.calculator.databinding.FragmentStartBinding

class StartFragment: Fragment() {
    lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signInFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}