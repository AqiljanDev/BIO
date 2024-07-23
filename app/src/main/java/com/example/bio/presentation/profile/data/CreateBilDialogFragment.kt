package com.example.bio.presentation.profile.data

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bio.R
import com.example.bio.databinding.FragmentCreateBilDialogBinding
import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill

class CreateBilDialogFragment : DialogFragment() {

    private var listener: BillDialog? = null

    interface BillDialog {
        fun getData(): FindOneOrderUserBill
        fun setData(bills: FindOneOrderUserBill)
    }

    private val binding: FragmentCreateBilDialogBinding by lazy {
        FragmentCreateBilDialogBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? BillDialog
        if (listener == null) {
            throw ClassCastException("$context must implement BillDialog")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = listener?.getData()

        data?.let {
            if (it.id == -1) {
                binding.tvDialogTitle.text = "Создать БИЛ"
                binding.btnCreate.text = "Создать"
            } else {
                binding.tvDialogTitle.text = it.bank
                binding.editTextBik.setText(it.code)
                binding.editTextIik.setText(it.kbe)
                binding.editTextBank.setText(it.bank)
                binding.btnCreate.text = "Обновить"
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnCreate.setOnClickListener {
            val bik = binding.editTextBik.text.toString()
            val iik = binding.editTextIik.text.toString()
            val bank = binding.editTextBank.text.toString()

            if (data != null) {
                listener?.setData(
                    data.apply {
                        code = bik
                        kbe = iik
                        this.bank = bank
                    }
                )
            }

            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateBilDialogFragment()
    }
}
