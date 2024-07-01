package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bio.R
import com.example.bio.databinding.CompareItemElementBinding
import com.example.bio.domain.entities.cart.ProductMiniCard
import com.example.bio.domain.entities.compare.CharactersToCompare
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.compare.ProductCompare
import com.example.bio.domain.entities.compare.ProductWrapper
import com.example.bio.presentation.data.CustomCompare

class CompareElementAdapter(
    private var compareList: List<CustomCompare>,
    private var products: List<ProductMiniCard>,
    private val clickToEventCompare: (prodId: String) -> Unit,
    private val clickToEventBasket: (prodId: String, count: Int) -> Unit,
    private val clickDeleteBasket: (id: Int) -> Unit,
    private val clickToCard: (slug: String) -> Unit
) : ListAdapter<CustomCompare, CompareElementAdapter.CompareItemElementViewHolder>(
    CompareElementDiffCallback()
) {

    inner class CompareItemElementViewHolder(
        private val binding: CompareItemElementBinding
    ) : ViewHolder(binding.root) {

        private var currentCount = 0

        @SuppressLint("PrivateResource")
        fun bind(product: CustomCompare, position: Int) = with(binding) {

            if (product.title == "") {
                imageViewPhoto.visibility = View.GONE
                imageViewDelete.visibility = View.GONE
                tvTitle.visibility = View.GONE

                tvCountMax.visibility = View.GONE
                btnBasket.visibility = View.GONE
                llBasketActive.visibility = View.GONE
                llPrices.visibility = View.GONE

                cardViewRoot.cardElevation = 0f
                cardViewRoot.setBackgroundResource(R.drawable.border_background)
                rcCharacters.adapter = CompareCharactersAdapter(product.characters)

                return
            }

            tvTitle.text = product.title
            tvPrice.text = "${product.price} ₸"
            tvCountMax.text = "Наличие:  ${product.count} шт"
            rcCharacters.adapter = CompareCharactersAdapter(product.characters)

            if (product.photo != null) {
                Glide.with(imageViewPhoto.context)
                    .load("http://192.168.8.3:4040/img/products/${product.photo}")
                    .placeholder(R.drawable.camera_slash) // Плейсхолдер до загрузки изображения
                    .error(R.drawable.camera_slash)
                    .into(imageViewPhoto)
            }

            imageViewDelete.setOnClickListener {
                clickToEventCompare(product.id1c)

                submitList(compareList.filter { it.id != product.id })
            }

            btnBasket.setOnClickListener {
                currentCount = 1
                clickToEventBasket(product.id1c, currentCount)
                tvCountMy.text = currentCount.toString()

                llBasketActive.visibility = View.VISIBLE
                tvCountMax.visibility = View.VISIBLE
                btnBasket.visibility = View.GONE
            }

            btnMinus.setOnClickListener {
                if (currentCount == 1) {
                    clickDeleteBasket(getProductId(product.id1c))

                    llBasketActive.visibility = View.GONE
                    tvCountMax.visibility = View.GONE
                    btnBasket.visibility = View.VISIBLE
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
        } else {
            llBasketActive.visibility = View.VISIBLE
            tvCountMax.visibility = View.VISIBLE
            btnBasket.visibility = View.GONE
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
        holder.bind(data, position)
    }

    fun updateLists(
        newlist: List<CustomCompare>,
        products: List<ProductMiniCard>
    ) {
        compareList = newlist
        this.products = products
        submitList(compareList)
    }

    fun checkBasket(id1c: String): Int {
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

private class CompareElementDiffCallback : DiffUtil.ItemCallback<CustomCompare>() {
    override fun areItemsTheSame(oldItem: CustomCompare, newItem: CustomCompare): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CustomCompare, newItem: CustomCompare): Boolean {
        return oldItem.title == newItem.title
    }
}



