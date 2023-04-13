package com.example.calculator.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculator.DTO.UserDTO
import com.example.calculator.R
import com.example.calculator.databinding.FragmentSignUpBinding
import com.example.calculator.remote.OkHttp.Okhttp
import com.example.calculator.viewModel.UserViewModel

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.btnRegistration.setOnClickListener {
            val okhttp = Okhttp()
            okhttp.registration(
                UserDTO(
                    binding.etFirstName.text.toString(),
                    binding.etLastName.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            UserViewModel.loadUser(
                UserDTO(
                    binding.etFirstName.text.toString(),
                    binding.etLastName.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            //findNavController().navigate(R.id.action_signUpFragment_to_userProfileFragment)
        }
        binding.btnBackSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnRegistrationToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}