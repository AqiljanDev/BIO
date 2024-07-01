package com.example.bio.presentation.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.bio.R
import com.example.bio.presentation.MainActivity

open class BaseBottomFragment : Fragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("Mylog", "This is finish catalog")
                AlertDialog.Builder(requireContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit_application)
                    .setPositiveButton(
                        R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            (activity as MainActivity).finishAffinity()
                        })
                    .setNegativeButton(R.string.no, null)
                    .show()
            }

        })
    }
}