package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.data.dto.compare.ProductWrapperDto
import com.example.bio.databinding.VerticalListBinding
import com.example.bio.domain.entities.compare.ProductWrapper

class CompareCharactersAdapter(
    private var list: List<String>
) : ListAdapter<ProductWrapper, CompareCharactersAdapter.VerticalListItemViewHolder>(
    CompareCharacterDiffCallback()
) {

    inner class VerticalListItemViewHolder(
        private val binding: VerticalListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(char: ProductWrapper) {
            Log.d("Mylog", "Vertical item = ${char.product.title}")
            binding.rcCharactersCard.adapter = CompareCharactersCardAdapter(list, char.product.characters)
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

    fun updateList(listCharacters: List<String>, listCustomCompare: List<ProductWrapper>) {
        list = listCharacters
        submitList(listCustomCompare)
    }
}

private class CompareCharacterDiffCallback : DiffUtil.ItemCallback<ProductWrapper>() {
    override fun areItemsTheSame(oldItem: ProductWrapper, newItem: ProductWrapper): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductWrapper, newItem: ProductWrapper): Boolean {
        return oldItem as ProductWrapperDto == newItem as ProductWrapperDto
    }
}

