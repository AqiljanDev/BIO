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
import com.example.data.SharedPreferencesManager
import com.example.login.LoginActivity

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

    private val viewModel: LoginViewModel by viewModels()
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

            Log.d("Mylog", "Email: $email, Pass: $pass")

            viewModel.login(LoginData(email, pass))
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_passwordRestoreFragment)
        }

        viewModel.stateLogin.onEach {

            when (it) {
                is StateSealedClass.Loading -> {
                    binding.tvIncorrectFormat.visibility = View.GONE
                }

                is StateSealedClass.Success -> {

                    Log.d("Mylog", "token: ${it.token}")
                    val sharedPreferences = SharedPreferencesManager.getInstance(requireContext())
                    sharedPreferences.putString(SharedPreferencesManager.KEYS.TOKEN, it.token)

                    (activity as LoginActivity).finish()
                    Log.d("Mylog", "Open account")
                }

                is StateSealedClass.Failed -> {
                    binding.tvIncorrectFormat.visibility = View.VISIBLE
                    binding.tvIncorrectFormat.text = it.message

                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}