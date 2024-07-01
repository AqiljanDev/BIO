package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bio.databinding.BasketProductBinding
import com.example.bio.domain.entities.cart.CartFullProduct
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOne.Product
import java.text.NumberFormat
import java.util.Locale

class BasketAdapter(
    private var products: List<CartFullProduct>,
    private val clickToEvent: (prodId: String, count: Int) -> Unit,
    private val clickDeleteBasket: (id: Int, totalPrice: Int) -> Unit,
    private val clickToCard: (product: Product) -> Unit,
    private val updateTotalPrice: (Int) -> Unit // New callback to update the total price
) : ListAdapter<CartFullProduct, BasketAdapter.BasketProductViewHolder>(CartDiffCallback()) {

    inner class BasketProductViewHolder(
        private val binding: BasketProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val formatMoney = NumberFormat.getNumberInstance(Locale("ru", "RU"))

        @SuppressLint("SetTextI18n")
        fun bind(cart: CartFullProduct) = with(binding) {
            var currentCount = cart.count

            tvTitle.text = cart.product.title
            tvPriceOfOne.text = "${formatMoney.format(cart.product.price * 68 / 100)} ₸"
            tvCountMax.text = "Наличие:\n ${cart.product.count} шт"
            tvCountMy.text = cart.count.toString()

            tvPrice.text = "${formatMoney.format((cart.count * cart.product.price) * 68 / 100)} ₸"
            tvPriceAction.text = "${formatMoney.format(cart.count * cart.product.price)} ₸"
            tvPriceAction.paintFlags = tvPriceAction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (cart.product.gallery.isNotEmpty()) {
                Glide.with(binding.root)
                    .load("http://192.168.0.103:4040/img/products/${cart.product.gallery[0]}")
                    .into(binding.imageViewPhoto)
            }

            btnMinus.alpha = if (currentCount == 1) 0.5f else 1.0f
            btnPlus.alpha = if (currentCount == cart.product.count) 0.5f else 1.0f

            btnMinus.setOnClickListener {
                if (currentCount == 1) return@setOnClickListener

                currentCount--
                binding.tvCountMy.text = currentCount.toString()

                tvPrice.text =
                    "${formatMoney.format((currentCount * cart.product.price) * 68 / 100)} ₸"
                tvPriceAction.text = "${formatMoney.format(currentCount * cart.product.price)} ₸"

                clickToEvent(cart.prodId, currentCount)
                btnMinus.alpha = if (currentCount == 1) 0.5f else 1.0f
                btnPlus.alpha = if (currentCount == cart.product.count) 0.5f else 1.0f
                updateTotalPrice(getTotalPrice())
            }

            btnPlus.setOnClickListener {
                if (currentCount == cart.product.count) return@setOnClickListener

                currentCount++
                tvCountMy.text = currentCount.toString()

                tvPrice.text =
                    "${formatMoney.format((currentCount * cart.product.price) * 68 / 100)} ₸"
                tvPriceAction.text = "${formatMoney.format(currentCount * cart.product.price)} ₸"

                clickToEvent(cart.prodId, currentCount)
                btnPlus.alpha = if (currentCount == cart.product.count) 0.5f else 1.0f
                btnMinus.alpha = if (currentCount == 1) 0.5f else 1.0f
                updateTotalPrice(getTotalPrice())
            }

            imageViewGarbage.setOnClickListener {
                clickDeleteBasket(cart.id, getTotalPrice() - (currentCount * cart.product.price * 68 / 100))
                updateLists(products.filter { it.id != cart.id })
                updateTotalPrice(getTotalPrice())
            }

            root.setOnClickListener {
                clickToCard(cart.product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketProductViewHolder {
        val binding = BasketProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BasketProductViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: BasketProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun getTotalPrice(): Int {
        return products.sumOf { (it.count * it.product.price) * 68 / 100 }
    }

    fun updateLists(newList: List<CartFullProduct>) {
        products = newList
        submitList(newList)
        updateTotalPrice(getTotalPrice()) // Update total price when the list changes
    }
}


class CartDiffCallback : DiffUtil.ItemCallback<CartFullProduct>() {
    override fun areItemsTheSame(oldItem: CartFullProduct, newItem: CartFullProduct): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartFullProduct, newItem: CartFullProduct): Boolean {
        return oldItem.product.title == newItem.product.title
    }
}
