package com.example.login.ui.main.login

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.login.R
import com.example.login.data.Repository
import com.example.login.data.api.retrofitAuth
import com.example.login.data.dataClass.LoginData
import com.example.login.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.login.ui.main.StateSealedClass
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }
    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.textInputEditEmail.text.toString()
            val pass = binding.textInputEditPass.text.toString()

            Log.d("Mylog", "Email = $email, Pass = $pass")
            viewModel.login(LoginData(email, pass))
        }

        viewModel.stateLogin.onEach {
            Log.d("Mylog", "State login onEach = IT: $it")

            when (it) {
                is StateSealedClass.Loading -> {
                    binding.tvIncorrectFormat.visibility = View.GONE
                }

                is StateSealedClass.Success -> {

                    Log.d("Mylog", "Start test get()")
                    try {
//                        retrofitAuth.testGet("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJxbmlja3NlbEBnbWFpbC5jb20iLCJjb21wYW55IjoiQ29tcGFueSIsIm1haW5BZG1pbiI6MSwiaWF0IjoxNzE3NDc1MzYzLCJleHAiOjE3MTgwODAxNjN9.XHUTSrKydDHNPe4wmAO4B5pIl__xHCFxD7lqDTuDqgA")

                        findNavController().navigate(R.id.action_loginFragment_to_tempFragment)

                        Log.d("Mylog", "Open account")

                    } catch (ex: Exception) {
                        Log.d("Mylog", "Exception message = ${ex.message}")
                    }
                }

                is StateSealedClass.Failed -> {
                    binding.tvIncorrectFormat.visibility = View.VISIBLE

                    Log.d("Mylog", "Failed class === Start test get()")
                    try {
                        retrofitAuth.testGet("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJxbmlja3NlbEBnbWFpbC5jb20iLCJjb21wYW55IjoiQ29tcGFueSIsIm1haW5BZG1pbiI6MSwiaWF0IjoxNzE3NDc1MzYzLCJleHAiOjE3MTgwODAxNjN9.XHUTSrKydDHNPe4wmAO4B5pIl__xHCFxD7lqDTuDqgA")

                        findNavController().navigate(R.id.action_loginFragment_to_tempFragment)

                        Log.d("Mylog", "Failed class === Open account")

                    } catch (ex: Exception) {
                        Log.d("Mylog", "Failed class === Exception message = ${ex.message}")
                    }

                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}