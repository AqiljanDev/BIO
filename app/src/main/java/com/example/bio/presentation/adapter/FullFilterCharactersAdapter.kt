package com.example.bio.presentation.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.R
import com.example.bio.data.dto.collectCharacters.BrandDto
import com.example.bio.databinding.FullFilterCharactersViewHolderBinding
import com.example.bio.domain.entities.collectCharacters.Brand
import com.google.android.material.chip.Chip

class FullFilterCharactersAdapter(
    private var listActive: List<String>
) : ListAdapter<Brand, FullFilterCharactersAdapter.FilterViewHolder>(BrandDiffCallback()) {

    class FilterViewHolder(private val binding: FullFilterCharactersViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: Brand, listActive: List<String>) = with(binding) {
            tvTitle.text = brand.title
            chipGroup.removeAllViews()
            for (character in brand.characters) {
                val chip = Chip(chipGroup.context).apply {
                    text = character.title
                    setChipBackgroundColorResource(R.color.white_standart_active)
                    setTextColor(Color.WHITE)
                    chipStartPadding = 5f
                    chipEndPadding = 5f
                    setEnsureMinTouchTargetSize(false)
                    chipStrokeWidth = 0f
                    isClickable = true
                    isCheckable = false
                    setOnClickListener {
                        setChipBackgroundColorResource(R.color.button_default_color)
                    }
                }
                chipGroup.addView(chip)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = FullFilterCharactersViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d("Mylog", "On create view holder f-f-ch")
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val brand = getItem(position)
        holder.bind(brand, listActive)
    }

    fun updateList(characters: List<Brand>, split: List<String>) {
        listActive = split
        submitList(characters)
    }

    class BrandDiffCallback : DiffUtil.ItemCallback<Brand>() {
        override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem as BrandDto == newItem as BrandDto
        }
    }
}


