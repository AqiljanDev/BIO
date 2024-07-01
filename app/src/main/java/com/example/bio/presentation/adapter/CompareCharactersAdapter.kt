package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.R
import com.example.bio.databinding.CompareCharactersItemBinding
import com.example.bio.domain.entities.cart.CartFullProduct
import com.example.bio.domain.entities.compare.CharactersToCompare

class CompareCharactersAdapter(
    private val list: List<String>
) : RecyclerView.Adapter<CompareCharactersAdapter.CompareCharacterItemViewHolder>() {

    inner class CompareCharacterItemViewHolder(
        private val binding: CompareCharactersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(char: String, position: Int) {
            binding.tvTitle.text = char

            binding.tvTitle.setBackgroundResource(
                if (position % 2 == 0) {
                    R.drawable.background_characters_active
                } else R.drawable.backgroud_characters_passive
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompareCharacterItemViewHolder {
        val binding = CompareCharactersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CompareCharacterItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompareCharacterItemViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        Log.d("Mylog", "Item size : ${list.size} = $list")
        return list.size
    }

}
