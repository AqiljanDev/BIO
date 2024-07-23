package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bio.R
import com.example.bio.databinding.BasketProductBinding
import com.example.bio.domain.entities.cart.CartFullProduct
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.core.UrlConstants
import java.text.NumberFormat
import java.util.Locale

class BasketAdapter(
    private var products: List<CartFullProduct>,
    private var listProfileDiscount: List<UserDiscount>,
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

            val discount = cart.product.discountPrice(listProfileDiscount)
            val price = discount.price

            when(discount.discountType) {
                0 -> {
                    tvPriceAction.visibility = View.GONE
                    tvProcent.visibility = View.GONE
                }

                1 -> {
                    tvPriceAction.visibility = View.VISIBLE
                    tvProcent.visibility = View.VISIBLE
                    tvProcent.text = "${discount.discountValue}%"
                    tvProcent.setBackgroundResource(R.drawable.button_procent_background)
                }

                2 -> {
                    tvPriceAction.visibility = View.VISIBLE
                    tvProcent.visibility = View.VISIBLE
                    tvProcent.text = "${formatMoney.format(discount.discountValue)}₸"
                    tvProcent.setBackgroundResource(R.drawable.button_money_background)
                }
            }

            tvTitle.text = cart.product.title
            tvPriceOfOne.text = "${formatMoney.format(price)} ₸"
            tvCountMax.text = "Наличие:\n ${cart.product.count} шт"
            tvCountMy.text = cart.count.toString()

            tvPrice.text = "${formatMoney.format(cart.count * price)} ₸"
            tvPriceAction.text = "${formatMoney.format(cart.count * cart.product.price)} ₸"
            tvPriceAction.paintFlags = tvPriceAction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (cart.product.gallery.isNotEmpty()) {
                Glide.with(binding.root)
                    .load( UrlConstants.IMG_PRODUCT_URL + cart.product.gallery[0])
                    .into(binding.imageViewPhoto)
            }

            btnMinus.alpha = if (currentCount == 1) 0.5f else 1.0f
            btnPlus.alpha = if (currentCount == cart.product.count) 0.5f else 1.0f

            btnMinus.setOnClickListener {
                if (currentCount == 1) return@setOnClickListener

                currentCount--
                binding.tvCountMy.text = currentCount.toString()

                tvPrice.text =
                    "${formatMoney.format(currentCount * price)} ₸"
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
                    "${formatMoney.format(currentCount * price)} ₸"
                tvPriceAction.text = "${formatMoney.format(currentCount * cart.product.price)} ₸"

                clickToEvent(cart.prodId, currentCount)
                btnPlus.alpha = if (currentCount == cart.product.count) 0.5f else 1.0f
                btnMinus.alpha = if (currentCount == 1) 0.5f else 1.0f
                updateTotalPrice(getTotalPrice())
            }

            imageViewGarbage.setOnClickListener {
                clickDeleteBasket(cart.id, getTotalPrice() - (currentCount * price))
                updateLists(products.filter { it.id != cart.id }, listProfileDiscount)
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
        return products.sumOf {
            it.count * it.product.discountPrice(listProfileDiscount).price
         }
    }

    fun updateLists(newList: List<CartFullProduct>, userDiscount: List<UserDiscount>) {
        products = newList
        listProfileDiscount = userDiscount
        submitList(newList)
        updateTotalPrice(getTotalPrice())
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
