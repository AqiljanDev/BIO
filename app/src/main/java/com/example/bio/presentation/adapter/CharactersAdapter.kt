package com.example.bio.presentation.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.R
import com.example.bio.data.dto.collectCharacters.BrandDto
import com.example.bio.databinding.CharacterViewGroupBinding
import com.example.bio.domain.entities.collectCharacters.Brand
import com.example.bio.domain.entities.collectCharacters.Character

class CharactersAdapter(
    private var listActive: List<String>,
    private val clickCharacter: (brand: BrandDto) -> Unit
) : ListAdapter<BrandDto, CharactersAdapter.CharacterViewHolder>(CharactersDiffUtil()) {

    private fun sortedList(list: List<BrandDto>) = list.sortedByDescending { brand ->
        brand.characters.any { it.id1c in listActive }
    }

    inner class CharacterViewHolder(private val binding: CharacterViewGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: BrandDto, isActive: Boolean) {
            val listActiveTitle = activeCharacters(brand)
            val title = if (isActive) {
                if (listActiveTitle.size == 1) {
                    listActiveTitle[0]
                } else "${listActiveTitle[0]} + ${listActiveTitle.size - 1}"
            } else brand.title
            binding.tvTitle.text = title

            binding.root.setBackgroundResource(
                if (isActive) R.drawable.button_background_main_active else R.drawable.button_background_main
            )

            binding.root.setOnClickListener {
                clickCharacter(brand)
            }
        }
    }

    fun activeCharacters(brand: BrandDto): List<String> {
        val list = mutableListOf<String>()
        brand.characters.forEach {
            if (listActive.contains(it.id1c)) {
                list.add(it.title)
            }
        }
        return list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterViewGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val srtList = sortedList(currentList)
        Log.d("Mylog", "Curr list = ${currentList.size}, sortedList.size = ${srtList.size}, pos = $position")
        if (srtList.isNotEmpty()) {
            val brand = srtList[position]
            val isActive = brand.characters.any { it.id1c in listActive }

            holder.bind(brand, isActive)
        }
    }

    fun updateList(list: List<BrandDto>, listActive: List<String>) {
        this.listActive = listActive
        submitList(list)

        if (list == currentList) {
            Log.d("Mylog", "Currlist == list")
            notifyDataSetChanged()
        }
    }
}

private class CharactersDiffUtil : DiffUtil.ItemCallback<BrandDto>() {
    override fun areItemsTheSame(oldItem: BrandDto, newItem: BrandDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BrandDto, newItem: BrandDto): Boolean {
        return oldItem == newItem
    }
}
