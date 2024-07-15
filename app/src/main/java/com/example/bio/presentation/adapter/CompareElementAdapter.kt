package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bio.R
import com.example.bio.data.dto.compare.ProductWrapperDto
import com.example.bio.databinding.CompareItemElementBinding
import com.example.bio.domain.entities.cart.ProductMiniCard
import com.example.bio.domain.entities.compare.ProductWrapper
import com.example.bio.domain.entities.userDiscount.UserDiscount
import java.text.NumberFormat
import java.util.Locale

class CompareElementAdapter(
    private var products: List<ProductMiniCard>,
    private var listProfileDiscount: List<UserDiscount>,
    private val clickToEventCompare: (prodId: String) -> Unit,
    private val clickToEventBasket: (prodId: String, count: Int) -> Unit,
    private val clickDeleteBasket: (id: Int) -> Unit,
    private val clickToCard: (slug: String) -> Unit
) : ListAdapter<ProductWrapper, CompareElementAdapter.CompareItemElementViewHolder>(
    CompareElementDiffCallback()
) {
    private var compareList: List<ProductWrapper> = listOf()
    private val formatMoney = NumberFormat.getNumberInstance(Locale("ru", "RU"))

    inner class CompareItemElementViewHolder(
        private val binding: CompareItemElementBinding
    ) : ViewHolder(binding.root) {

        private var currentCount = 0

        @SuppressLint("PrivateResource", "SetTextI18n")
        fun bind(productWrapper: ProductWrapper) = with(binding) {
            val product = productWrapper.product
            Log.d("Mylog", "Update bind \\ ${product.title} \\ = charact = ${product.characters.size}")
            currentCount = checkBasket(product.id1c)

            if (product.title == "") {
                imageViewPhoto.visibility = View.GONE
                imageViewDelete.visibility = View.GONE
                tvTitle.visibility = View.GONE

                tvCountMax.visibility = View.GONE
                btnBasket.visibility = View.GONE
                tvCountState.visibility = View.GONE
                llBasketActive.visibility = View.GONE
                llPrices.visibility = View.GONE

                cardViewRoot.cardElevation = 0f
                cardViewRoot.setBackgroundResource(R.drawable.border_background)

                return
            }

            val discount = product.discountPrice(listProfileDiscount)

            tvPriceAction.visibility = if (discount.discountType == 0) View.GONE else View.VISIBLE
            tvPrice.text = "${formatMoney.format(discount.price)} ₸"
            tvPriceAction.text = "${product.price}"
            tvPriceAction.paintFlags = tvPriceAction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            tvTitle.text = product.title
            tvCountMax.text = "Наличие:  ${formatMoney.format(product.count)} шт"
            tvCountMy.text = currentCount.toString()

            if (product.photo != null) {
                Glide.with(imageViewPhoto.context)
                    .load("http://192.168.8.3:4040/img/products/${product.photo}")
                    .placeholder(R.drawable.camera_slash) // Плейсхолдер до загрузки изображения
                    .error(R.drawable.camera_slash)
                    .into(imageViewPhoto)
            }

            imageViewDelete.setOnClickListener {
                clickToEventCompare(product.id1c)

                updateLists(compareList.filter { it.id != productWrapper.id }, products, listProfileDiscount)
            }

            btnBasket.setOnClickListener {
                currentCount = 1
                clickToEventBasket(product.id1c, currentCount)
                tvCountMy.text = currentCount.toString()

                llBasketActive.visibility = View.VISIBLE
                tvCountMax.visibility = View.VISIBLE
                btnBasket.visibility = View.GONE
                tvCountState.visibility = View.GONE
            }

            btnMinus.setOnClickListener {
                if (currentCount == 1) {
                    clickDeleteBasket(getProductId(product.id1c))

                    llBasketActive.visibility = View.GONE
                    tvCountMax.visibility = View.GONE
                    btnBasket.visibility = View.VISIBLE
                    tvCountState.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                currentCount--
                tvCountMy.text = currentCount.toString()

                clickToEventBasket(product.id1c, currentCount)
            }

            btnPlus.setOnClickListener {
                if (currentCount == product.count) return@setOnClickListener

                currentCount++
                tvCountMy.text = currentCount.toString()

                clickToEventBasket(product.id1c, currentCount)
            }

            root.setOnClickListener {
                clickToCard(product.slug)
            }

            checkCurrentCount(product.id1c, binding)
        }

    }

    private fun checkCurrentCount(
        id1c: String,
        binding: CompareItemElementBinding
    ) = with(binding) {
        val count = checkBasket(id1c)
        if (count <= 0) {
            llBasketActive.visibility = View.GONE
            tvCountMax.visibility = View.GONE
            btnBasket.visibility = View.VISIBLE
            tvCountState.visibility = View.VISIBLE
        } else {
            llBasketActive.visibility = View.VISIBLE
            tvCountMax.visibility = View.VISIBLE
            btnBasket.visibility = View.GONE
            tvCountState.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompareItemElementViewHolder {
        Log.d("Mylog", "ADAPTER = ON CREATE VIEW HOLDER")
        val binding = CompareItemElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompareItemElementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompareItemElementViewHolder, position: Int) {
        Log.d("Mylog", "ADAPTER = ON BIND VIEW HOLDER")
        val data = compareList[position]
        holder.bind(data)
    }

    fun updateLists(
        newlist: List<ProductWrapper>,
        products: List<ProductMiniCard>,
        listProfileDiscount: List<UserDiscount>
    ) {

        compareList = newlist
        this.listProfileDiscount = listProfileDiscount
        this.products = products
        submitList(compareList)
    }

    private fun checkBasket(id1c: String): Int {
        var res = 0
        products.forEach {
            if (it.prodId == id1c) res = it.count
        }
        return res
    }

    fun getProductId(id1c: String): Int {
        products.forEach {
            if (it.prodId == id1c) return it.id
        }
        return 0
    }
}

private class CompareElementDiffCallback : DiffUtil.ItemCallback<ProductWrapper>() {
    override fun areItemsTheSame(oldItem: ProductWrapper, newItem: ProductWrapper): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductWrapper, newItem: ProductWrapper): Boolean {
        return oldItem as ProductWrapperDto == newItem as ProductWrapperDto
    }
}



