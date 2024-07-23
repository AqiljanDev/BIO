package com.example.bio.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.data.dto.CabinetDto
import com.example.bio.databinding.EditTextItemViewBinding
import com.example.bio.domain.entities.Cabinet
import com.example.bio.presentation.data.PairData
import com.google.android.material.textfield.TextInputEditText

class CabinetAdapter : ListAdapter<PairData, CabinetAdapter.EditTextViewHolder>(DiffCallback()) {

    // Список для хранения ссылок на все TextInputEditText в адаптере
    private val editTextList = mutableListOf<TextInputEditText>()

    inner class EditTextViewHolder(
        private val binding: EditTextItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PairData) {
            binding.textInputLayoutText.hint = data.first
            binding.textInputEditText.setText(data.last ?: "Пусто") // Пример привязки данных
        }

        init {
            // Добавляем TextInputEditText в список
            editTextList.add(binding.textInputEditText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTextViewHolder {
        val binding = EditTextItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditTextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditTextViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    private fun collectEditTextValues(): List<String> {
        return editTextList.map { it.text.toString() }
    }

    fun createCabinetObject(): Cabinet {
        val values = collectEditTextValues()
        if (values.size < 10) throw IllegalStateException("Not enough values to create Cabinet object")
        return CabinetDto(
            phone = values[0],
            company = values[1],
            type = values[2],
            area = values[3],
            site = values[4],
            bin = values[5],
            address = values[6],
            bik = values[7],
            bank = values[8],
            iik = values[9]
        )
    }


    class DiffCallback : DiffUtil.ItemCallback<PairData>() {
        override fun areItemsTheSame(oldItem: PairData, newItem: PairData): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(oldItem: PairData, newItem: PairData): Boolean {
            return oldItem == newItem
        }
    }
}

