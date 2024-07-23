package com.example.login.ui.main.passwordRestore

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.login.R
import com.example.login.data.dataClass.PasswordCodeSendData
import com.example.login.databinding.FragmentPasswordRestoreBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PasswordRestoreFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordRestoreFragment()
    }

    private val viewModel: PasswordRestoreViewModel by viewModels()
    private val binding: FragmentPasswordRestoreBinding by lazy {
        FragmentPasswordRestoreBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateRestore.onEach {

            if (it) {
                binding.tvIncorrectFormat.visibility = View.GONE
                binding.textInputLayoutEmail.visibility = View.GONE
                binding.btnGetKey.visibility = View.GONE

                binding.tvSentEmail.visibility = View.VISIBLE
            } else {
                binding.tvIncorrectFormat.visibility = View.VISIBLE
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnGetKey.setOnClickListener {
            val editText = binding.textInputEditEmail.text.toString()

            viewModel.restoreSendCode(
                PasswordCodeSendData(editText)
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}