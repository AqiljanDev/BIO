package com.example.bio.presentation.filter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bio.R
import com.example.bio.databinding.FragmentSheetBinding
import com.example.bio.domain.entities.collectCharacters.Character
import com.example.bio.presentation.adapter.FilterCharactersAdapter
import com.example.bio.utils.toggleItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentSheetBinding by lazy {
        FragmentSheetBinding.inflate(layoutInflater)
    }

    private var listener: BottomSheetListener? = null

    interface BottomSheetListener {
        fun onApplyClicked(char: List<String>)
        fun onCharactersList(): List<Character>

        fun getId1cActiveList(): List<String>
        fun getTitleBrand(): String
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
        val listCharacterId: MutableList<String> = mutableListOf()
        val adapter = FilterCharactersAdapter(listOf(), listOf()) {
            Log.d("Mylog", "CharId = $it")
            listCharacterId.toggleItem(it)
        }
        binding.rcCharacters.adapter = adapter

        binding.imageViewClose.setOnClickListener {
            dismiss()
        }

        binding.btnReset.setOnClickListener {
            // Реализуйте логику сброса
        }

        binding.btnEnter.setOnClickListener {
            listener?.onApplyClicked(listCharacterId)
            Log.d("Mylog", "Btn enter = $listCharacterId")
            dismiss()
        }

        val list = listener?.onCharactersList()
        val listActive = listener?.getId1cActiveList() ?: listOf()
        list?.let { character ->
            adapter.updateList(character, listActive)
        }
        binding.tvTitle.text = listener?.getTitleBrand()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SheetFragment()
    }
}
