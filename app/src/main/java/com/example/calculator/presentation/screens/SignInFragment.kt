package com.example.calculator.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.calculator.DTO.UserDTO
import com.example.calculator.R
import com.example.calculator.databinding.FragmentSignInBinding
import com.example.calculator.remote.OkHttp.Okhttp
import com.example.calculator.utils.Constants.TAG
import com.example.calculator.viewModel.TokenViewModel
import com.example.calculator.viewModel.UserViewModel
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment() {
    lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)


        binding.btnBackSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLoginToRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        lifecycleScope.launch {
            TokenViewModel.stateToken.collect {
                Log.d(TAG, it?.firstName ?: "еще не присвоилось значение")
                if (it != null)
                    findNavController().navigate(R.id.action_signInFragment_to_userProfileFragment)
            }
        }
        /*lifecycleScope.launch {
            ErrorViewModel.stateError.collect {
                if (it != null)
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }*/
        return binding.root
    }

    override fun onStart() {
        binding.btnLogination.setOnClickListener {
            val okhttp = Okhttp()
            try {
                okhttp.registration(
                    UserDTO(
                        "",
                        "",
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                )
            } catch (ex: Exception) {
                Toast.makeText(context, "Неверные данные", Toast.LENGTH_LONG).show()
            }
            UserViewModel.loadUser(
                UserDTO(
                    "",
                    "",
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
            //
        }
        super.onStart()
    }
}