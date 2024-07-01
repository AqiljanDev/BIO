package com.example.bio.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.R
import com.example.bio.databinding.CharacterCardBinding
import com.example.bio.domain.entities.findOne.CharactersToProducts
import com.example.bio.presentation.data.CharsCard

class CharactersCardAdapter(
    private val list: List<CharactersToProducts>
) : RecyclerView.Adapter<CharactersCardAdapter.CharacterCardViewHolder>(){
    private var random = 0

    inner class CharacterCardViewHolder(private val binding: CharacterCardBinding) : ViewHolder(binding.root) {

        fun bind(char: CharactersToProducts) {
            binding.tvTitle.text = char.character.title
            binding.tvValue.text = char.characterValue.title

            if (random % 2 == 0) binding.root.setBackgroundResource(R.color.background_button_clear)

            random += 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterCardViewHolder {
        val data = CharacterCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CharacterCardViewHolder(data)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CharacterCardViewHolder, position: Int) {
        val data = list[position]

        holder.bind(data)
    }


}