package com.example.login.ui.main.register

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.data.SharedPreferencesManager
import com.example.login.R
import com.example.login.data.dataClass.RegisterData
import com.example.login.databinding.FragmentRegisterBinding
import com.example.login.ui.main.StateSealedClass
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
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

//                viewModel.register(registerData)
                findNavController().navigate(R.id.action_registerFragment_to_statusPendingFragment)
            }
        }


        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.company_type)
        )
        binding.spinnerTypeCompany.adapter = spinnerAdapter


        viewModel.stateRegister.onEach {

            when (it) {
                is StateSealedClass.Loading -> {
                    binding.tvIncorrectFormat.visibility = View.GONE
                }

                is StateSealedClass.Success -> {
                    val sharedPreferences = SharedPreferencesManager.getInstance(requireContext())
                    sharedPreferences.putString(SharedPreferencesManager.KEYS.TOKEN, it.token)

                    findNavController().navigate(R.id.action_registerFragment_to_statusPendingFragment)
                }

                is StateSealedClass.Failed -> {

                    binding.tvIncorrectFormat.visibility = View.VISIBLE
                    binding.tvIncorrectFormat.text = it.message
                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }
}