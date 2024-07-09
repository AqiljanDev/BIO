package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.R
import com.example.bio.databinding.CompareCharactersItemBinding
import com.example.bio.databinding.VerticalListBinding
import com.example.bio.domain.entities.cart.CartFullProduct
import com.example.bio.domain.entities.compare.CharactersToCompare
import com.example.bio.presentation.data.CustomCompare

class CompareCharactersAdapter(
    private var list: List<String>
) : ListAdapter<CustomCompare, CompareCharactersAdapter.VerticalListItemViewHolder>(
    CompareCharacterDiffCallback()
) {

    inner class VerticalListItemViewHolder(
        private val binding: VerticalListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(char: CustomCompare) {
            Log.d("Mylog", "Vertical item = ${char.title}")
            binding.rcCharactersCard.adapter = CompareCharactersCardAdapter(list, char.characters)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerticalListItemViewHolder {
        val binding = VerticalListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VerticalListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerticalListItemViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    fun updateList(listCharacters: List<String>, listCustomCompare: List<CustomCompare>) {
        list = listCharacters
        submitList(listCustomCompare)
    }
}

private class CompareCharacterDiffCallback : DiffUtil.ItemCallback<CustomCompare>() {
    override fun areItemsTheSame(oldItem: CustomCompare, newItem: CustomCompare): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CustomCompare, newItem: CustomCompare): Boolean {
        return oldItem == newItem
    }
}

