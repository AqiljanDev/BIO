package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.databinding.SelectionCharactersBinding
import com.example.bio.domain.entities.collectCharacters.Brand
import com.example.bio.domain.entities.collectCharacters.Character

class FilterCharactersAdapter(
    private var list: List<Character>,
    private var listActive: List<String>,
    private val clickCheckBox: (characterId: String) -> Unit
) : ListAdapter<Character, FilterCharactersAdapter.SelectionCharactersViewHolder>(
    FilterCharacterDiffUtil()
) {

    init {
        submitList(list)
    }

    inner class SelectionCharactersViewHolder(
        private val binding: SelectionCharactersBinding
    ) : ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.tvTitle.text = character.title

            if (listActive.isNotEmpty()) {
                binding.checkBox.isChecked = (listActive.contains(character.id1c))
            }

            binding.checkBox.setOnCheckedChangeListener { _, _ ->
                clickCheckBox(character.id1c)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectionCharactersViewHolder {
        Log.d("Mylog", "on create view holder filter")
        val binding = SelectionCharactersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectionCharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectionCharactersViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)
    }

    fun updateList(list: List<Character>, listActive: List<String>) {
        this.listActive = listActive
        this.list = list
        submitList(list)
    }

}

private class FilterCharacterDiffUtil : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.title == newItem.title
    }

}