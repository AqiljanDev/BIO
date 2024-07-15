package com.example.bio.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.databinding.SelectionCharactersBinding
import com.example.bio.domain.entities.collectCharacters.Character
import com.example.bio.presentation.data.CharacterState

class FilterCharactersAdapter(
    private var characterStates: List<CharacterState>,
    private val clickCheckBox: (characterId: String) -> Unit
) : ListAdapter<CharacterState, FilterCharactersAdapter.SelectionCharactersViewHolder>(
    FilterCharacterStateDiffUtil()
) {

    init {
        submitList(characterStates)
    }

    inner class SelectionCharactersViewHolder(
        private val binding: SelectionCharactersBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(characterState: CharacterState) {
            binding.tvTitle.text = characterState.character.title
            binding.checkBox.isChecked = characterState.isActive

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                characterState.isActive = isChecked
                clickCheckBox(characterState.character.id1c)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectionCharactersViewHolder {
        val binding = SelectionCharactersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectionCharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectionCharactersViewHolder, position: Int) {
        val data = characterStates[position]
        holder.bind(data)
    }

    fun updateList(characters: List<Character>, activeIds: List<String>) {
        if (characters.isEmpty()) {
            characterStates = characterStates.map { CharacterState(it.character, false) }
            submitList(characterStates)
            notifyDataSetChanged()
            return
        }

        characterStates = characters.map { character ->
            CharacterState(character, activeIds.contains(character.id1c))
        }

        submitList(characterStates)
    }

    fun uncheckAll() {
        characterStates.forEach { it.isActive = false }
        // Обновляем видимые и не видимые элементы
        notifyDataSetChanged()
    }
}


private class FilterCharacterStateDiffUtil : DiffUtil.ItemCallback<CharacterState>() {
    override fun areItemsTheSame(oldItem: CharacterState, newItem: CharacterState): Boolean {
        return oldItem.character.id == newItem.character.id
    }

    override fun areContentsTheSame(oldItem: CharacterState, newItem: CharacterState): Boolean {
        return oldItem == newItem
    }
}


