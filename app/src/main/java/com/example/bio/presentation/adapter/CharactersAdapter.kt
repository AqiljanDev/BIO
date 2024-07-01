package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.databinding.CharacterViewGroupBinding
import com.example.bio.domain.entities.collectCharacters.Brand

class CharactersAdapter(
    private val list: List<Brand>,
    private val clickCharacter: (brand: Brand) -> Unit
) : RecyclerView.Adapter<CharactersAdapter.CharacterViwHolder>() {

    inner class CharacterViwHolder(private val binding: CharacterViewGroupBinding) :
        ViewHolder(binding.root) {

        fun bind(brand: Brand) {
            binding.tvTitle.text = brand.title

            binding.root.setOnClickListener {
                clickCharacter(brand)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViwHolder {
        val binding = CharacterViewGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViwHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CharacterViwHolder, position: Int) {
        val data = list[position]

        Log.d("Mylog", "CharactersAdapter = onBindViewHolder/ data = $data")

        holder.bind(data)
    }
}