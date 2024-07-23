package com.example.bio.presentation.filter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bio.R
import com.example.bio.databinding.FragmentSheetBinding
import com.example.bio.domain.entities.collectCharacters.Character
import com.example.bio.presentation.adapter.FilterCharactersAdapter
import com.example.bio.utils.toggleItem
import com.example.bio.utils.toggleItemBasedOnList
import com.example.bio.utils.toggleItems
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("DEPRECATION")
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
        var listCharactersCurrent: List<Character> = listOf()
        val listCharacterId: MutableList<String> = mutableListOf()
        val listActive = listener?.getId1cActiveList() ?: listOf()
        val adapter = FilterCharactersAdapter(mutableListOf()) {
            Log.d("Mylog", "CharId = $it")
            listCharacterId.toggleItem(it)
        }
        binding.rcCharacters.adapter = adapter

        binding.imageViewClose.setOnClickListener {
            dismiss()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.btnReset.setOnClickListener {
            adapter.resetAllCheckboxes() // Сброс к начальному состоянию
            listCharacterId.clear() // Очистите активные ID
            listCharacterId.addAll(listActive) // Верните в активные ID только те, которые были активны
            Log.d("Mylog", "Reset: listActive = ${listActive.size}, listCharId = ${listCharacterId.size}")
        }

        binding.btnEnter.setOnClickListener {
            listener?.onApplyClicked(listCharacterId)
            Log.d("Mylog", "Btn enter = $listCharacterId")
            dismiss()
        }

        val list = listener?.onCharactersList()
        list?.let { character ->
            adapter.updateList(character, listActive)
            listCharactersCurrent = character
        }
        binding.tvTitle.text = listener?.getTitleBrand()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SheetFragment()
    }
}


