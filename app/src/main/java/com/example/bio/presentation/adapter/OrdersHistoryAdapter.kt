package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.data.dto.myOrder.MyOrderDto
import com.example.bio.databinding.DiscountHistoryViewHolderBinding
import com.example.bio.domain.entities.myOrder.MyOrder
import java.text.NumberFormat
import java.util.Locale

class OrdersHistoryAdapter(
    private val clickOpenHistory: (id: Int) -> Unit
) : ListAdapter<MyOrder, OrdersHistoryAdapter.DiscountHistoryViewHolder>(
    HistoryDiffCallBack()
){
    private val formatMoney = NumberFormat.getNumberInstance(Locale("ru", "RU"))

    inner class DiscountHistoryViewHolder(
        private val binding: DiscountHistoryViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(order: MyOrder) = with(binding) {
            tvOrderNumber.text = "#${order.id}"
            tvDate.text = order.date
            tvStatus.text = order.orderStatus.name
            tvPrice.text = "${formatMoney.format( order.products.sumOf { it.price * it.count } )}₸"

            imageViewIcon.setOnClickListener {
                clickOpenHistory(order.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountHistoryViewHolder {
        val view = DiscountHistoryViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DiscountHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscountHistoryViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }


    class HistoryDiffCallBack : DiffUtil.ItemCallback<MyOrder>() {
        override fun areItemsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem as MyOrderDto == newItem as MyOrderDto
        }

    }
}