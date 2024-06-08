package com.example.login.ui.main.register

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.login.R
import com.example.login.data.dataClass.RegisterData
import com.example.login.databinding.FragmentRegisterBinding
import com.example.login.ui.main.StateSealedClass
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels { RegisterViewModelFactory() }
    private val binding: FragmentRegisterBinding by lazy {
        FragmentRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCreateAccount.setOnClickListener {
            with(binding) {
                val registerData = RegisterData(
                    textInputEditEmail.text.toString(),
                    textInputEditPhone.text.toString(),
                    textInputEditCompany.text.toString(),
                    spinnerTypeCompany.selectedItem.toString(),
                    textInputEditFieldActive.text.toString(),
                    textInputEditSiteCompany.text.toString(),
                    textInputEditBin.text.toString(),
                    textInputEditLegalAddress.text.toString(),
                    textInputEditBic.text.toString(),
                    textInputEditBank.text.toString(),
                    textInputEditAccountNumber.text.toString()
                )

                viewModel.register(registerData)
            }
        }

        viewModel.stateRegister.onEach {

            when (it) {
                is StateSealedClass.Loading -> {
                    binding.tvIncorrectFormat.visibility = View.GONE
                }

                is StateSealedClass.Success -> {
                    Toast.makeText(context, "Ты прошел регистрацию", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_registerFragment_to_statusPendingFragment)
                }

                is StateSealedClass.Failed -> {

                    Log.d("Mylog", "Failed = ${it.message}")

                    binding.tvIncorrectFormat.visibility = View.VISIBLE
                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }
}