package com.example.bio.presentation.profile.passwordRestore

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bio.R
import com.example.bio.databinding.FragmentPasswordRestoreProfileBinding
import com.example.bio.presentation.MainActivity
import com.example.data.SharedPreferencesManager
import com.example.login.data.dataClass.PasswordCodeSendData
import com.example.login.databinding.FragmentPasswordRestoreBinding
import com.example.login.ui.main.passwordRestore.PasswordRestoreFragment
import com.example.login.ui.main.passwordRestore.PasswordRestoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PasswordRestoreProfileFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordRestoreProfileFragment()
    }

    private val viewModel: PasswordRestoreProfileViewModel by viewModels()
    private val binding: FragmentPasswordRestoreProfileBinding by lazy {
        FragmentPasswordRestoreProfileBinding.inflate(layoutInflater)
    }

    private val sharedPreference by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the action bar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        // Hide the bottom navigation view
        (activity as? MainActivity)?.binding?.bottomNavigation?.visibility = View.GONE

        // Hide the navigation view
        (activity as? MainActivity)?.binding?.navView?.visibility = View.GONE

        // Disable the drawer layout from being opened
        (activity as? MainActivity)?.binding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

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
            viewModel.restoreSendCode(PasswordCodeSendData(editText))
        }

        binding.btnBack.setOnClickListener {
            sharedPreference.removeString(SharedPreferencesManager.KEYS.TOKEN)
            (activity as MainActivity).finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Show the action bar
        (activity as? AppCompatActivity)?.supportActionBar?.show()

        // Show the bottom navigation view
        (activity as? MainActivity)?.binding?.bottomNavigation?.visibility = View.VISIBLE

        // Show the navigation view
        (activity as? MainActivity)?.binding?.navView?.visibility = View.VISIBLE

        // Enable the drawer layout to be opened again
        (activity as? MainActivity)?.binding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}
