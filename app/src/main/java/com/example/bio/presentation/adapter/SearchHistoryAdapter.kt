package com.example.bio.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(
    private val history: List<String>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(text: String) {
            binding.searchHistoryText.text = text

            binding.llSearchHistory.setOnClickListener {
                onItemClicked(binding.searchHistoryText.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val query = history[position]
        holder.bind(query)
    }

    override fun getItemCount(): Int = history.size
}
