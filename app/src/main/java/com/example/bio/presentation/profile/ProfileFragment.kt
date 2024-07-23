package com.example.bio.presentation.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.bio.R
import com.example.bio.databinding.FragmentProfileBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.profile.data.ProfileDataFragment
import com.example.bio.presentation.profile.orders.OrdersFragment
import com.example.bio.presentation.profile.passwordRestore.PasswordRestoreProfileFragment
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseBottomFragment() {

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    private val sharedPreference: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, OrdersFragment())
                .commit()
        }
        replaceWindow(binding.btnOrders)

        binding.btnOrders.setOnClickListener {
            if (savedInstanceState == null) {
                childFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, OrdersFragment())
                    .commit()
            }
            replaceWindow(binding.btnOrders)
        }

        binding.btnData.setOnClickListener {
            if (savedInstanceState == null) {
                childFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, ProfileDataFragment())
                    .commit()
            }
            replaceWindow(binding.btnData)
        }

        binding.btnPassword.setOnClickListener {
            (activity as MainActivity).replacerFragment(PasswordRestoreProfileFragment())
        }

    }

    private fun replaceWindow(btn: AppCompatButton) = with(binding) {
        when(btn) {
            btnOrders -> {
                btnOrders.setBackgroundResource(R.drawable.button_background)
                btnOrders.setTextColor(requireContext().getColor(R.color.white))
                btnData.setBackgroundResource(R.drawable.button_background_passive)
                btnData.setTextColor(requireContext().getColor(R.color.black))
        }
            btnData -> {
                btnData.setBackgroundResource(R.drawable.button_background)
                btnData.setTextColor(requireContext().getColor(R.color.white))
                btnOrders.setBackgroundResource(R.drawable.button_background_passive)
                btnOrders.setTextColor(requireContext().getColor(R.color.black))
            }

            else -> {
                btnData.setBackgroundResource(R.drawable.button_background_passive)
                btnData.setTextColor(requireContext().getColor(R.color.black))
                btnOrders.setBackgroundResource(R.drawable.button_background_passive)
                btnOrders.setTextColor(requireContext().getColor(R.color.black))
            }

        }
    }

}