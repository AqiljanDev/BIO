package com.example.bio.presentation.adapter

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.databinding.CompareCharactersItemBinding

class CompareCharactersCardAdapter(
    private var listName: List<String>,
    private var listData: List<String>
) : RecyclerView.Adapter<CompareCharactersCardAdapter.CompareCharactersItemViewHolder>() {

    inner class CompareCharactersItemViewHolder(
        private val binding: CompareCharactersItemBinding
    ): ViewHolder(binding.root) {

        fun bind(position: Int) {
            Log.d("log", "list = $$listName, position = $position, listData = $listData,")
            binding.tvCharacterName.text = listName[position]
            binding.tvTitle.text = listData[position]

            if (listData[position] != "-") binding.tvTitle.gravity = Gravity.START
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompareCharactersItemViewHolder {
        val binding = CompareCharactersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d("Mylog", "On create view holder - CompareCharactersCardAdapter")
        return CompareCharactersItemViewHolder(binding)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CompareCharactersItemViewHolder, position: Int) {
        holder.bind(position)
    }

}