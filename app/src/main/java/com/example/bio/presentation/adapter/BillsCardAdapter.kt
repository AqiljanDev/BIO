package com.example.bio.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.data.dto.findOneOrder.FindOneOrderUserBillDto
import com.example.bio.databinding.BillsCardItemHolderBinding
import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill

class BillsCardAdapter(
    private val clickDelete: (id: Int) -> Unit,
    private val clickRefactor: (bills: FindOneOrderUserBill) -> Unit
) : ListAdapter<FindOneOrderUserBill, BillsCardAdapter.BillsCardViewHolder>(DiffCallback()) {

    inner class BillsCardViewHolder(private val binding: BillsCardItemHolderBinding) : ViewHolder(binding.root) {

        fun bind(bill: FindOneOrderUserBill) = with(binding) {
            tvBik.text = bill.code
            tvIik.text = bill.kbe
            tvBank.text = bill.bank

            btnDelete.setOnClickListener {
                clickDelete(bill.id)
            }

            btnRefactor.setOnClickListener {
                clickRefactor(bill)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsCardViewHolder {
        val view = BillsCardItemHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BillsCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillsCardViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }


    class DiffCallback : DiffUtil.ItemCallback<FindOneOrderUserBill>() {
        override fun areItemsTheSame(
            oldItem: FindOneOrderUserBill,
            newItem: FindOneOrderUserBill
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FindOneOrderUserBill,
            newItem: FindOneOrderUserBill
        ): Boolean {
            return oldItem as FindOneOrderUserBillDto == newItem as FindOneOrderUserBillDto
        }

    }
}