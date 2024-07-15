package com.example.bio.presentation.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import com.example.bio.R
import com.example.bio.databinding.FragmentSortSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentSortSheetBinding by lazy {
        FragmentSortSheetBinding.inflate(layoutInflater)
    }

    private var listener: BottomSheetListener? = null

    interface BottomSheetListener {
        fun onSortSelected(sort: FilterFragment.SORT)
        fun getCurrentSort(): FilterFragment.SORT
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? BottomSheetListener
        if (listener == null) {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentSort = listener?.getCurrentSort()

        FilterFragment.SORT.entries.forEach { sort ->
            val radioButton = RadioButton(context).apply {
                text = sort.translate
                isChecked = sort == currentSort

                textSize = 18f // Увеличиваем размер текста
                setPadding(20, 20, 20, 20) // Добавляем отступы
                typeface = ResourcesCompat.getFont(context, R.font.roboto_condensed_regular)

                setOnClickListener {
                    listener?.onSortSelected(sort)
                    dismiss()
                }
            }
            binding.radioGroupSort.addView(radioButton)


        }

        binding.imageViewClose.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SortSheetFragment()
    }
}
