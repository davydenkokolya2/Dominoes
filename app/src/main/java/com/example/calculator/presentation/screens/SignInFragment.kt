package com.example.calculator.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculator.DTO.UserDTO
import com.example.calculator.R
import com.example.calculator.databinding.FragmentSignInBinding
import com.example.calculator.remote.OkHttp.Okhttp
import com.example.calculator.viewModel.UserViewModel

class SignInFragment : Fragment() {
    lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.btnLogination.setOnClickListener {
            val okhttp = Okhttp()
            okhttp.registration(
                UserDTO(
                    "",
                    "",
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            UserViewModel.loadUser(
                UserDTO(
                    "",
                    "",
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            findNavController().navigate(R.id.action_signInFragment_to_userProfileFragment)
        }
        binding.btnBackSignIn.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.btnLoginToRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        return binding.root
    }
}