package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bio.R
import com.example.bio.data.dto.ProductDto
import com.example.bio.data.dto.WishListCompareMiniDto
import com.example.bio.databinding.ProductViewGroupBinding
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.wishList.WishListCompareMini

class CategoryAdapter(
    private val catalog: List<Product>,
    private var wishList: List<WishListCompareMini>,
    private var compareList: List<WishListCompareMini>,
    private var cart: CartMini,
    private val clickFavorite: (isState: Boolean, id1c: String) -> Unit,
    private val clickGroup: (isState: Boolean, id1c: String) -> Unit,
    private val clickEventBasket: (prodId: String, count: Int) -> Unit,
    private val clickDeleteBasket: (id: Int) -> Unit,
    private val clickToCard: (Product) -> Unit
) : ListAdapter<Product, CategoryAdapter.ProductViewHolder>(ProductDiffCallback()) {

    init {
        submitList(catalog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductViewGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ProductViewHolder(private val binding: ProductViewGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) = with(binding) {
            tvTitle.text = product.title
            tvApt.text = "apt: ${product.article}"
            Log.d("Mylog", "INNER CLASS BIND ${product.title}")

            if (product.charactersToProducts.isNotEmpty()) {
                val size = product.charactersToProducts.size
                val textViews = listOf(tvCharacterOne, tvCharacterTwo, tvCharacterThree)
                val llChar = listOf(llCharacterOne, llCharacterTwo, llCharacterThree)

                for (i in 0 until minOf(size, textViews.size)) {
                    llChar[i].visibility = View.VISIBLE
                    textViews[i].text =
                        product.charactersToProducts[i].character.title + ": " + product.charactersToProducts[i].characterValue.title
                }
            }

            tvPriceAction.paintFlags = tvPriceAction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (product.gallery.isNotEmpty()) {
                Glide.with(root.context)
                    .load("http://192.168.8.3:4040/img/products/" + product.gallery[0].photo)
                    .into(imageViewMain)
            }

            binding.imgBtnLike.setImageResource(
                if (btnLikeRealize(product.id1c)) R.drawable.ic_heath_active
                else R.drawable.heart_passive
            )

            binding.imgBtnGroup.setImageResource(
                if (btnGroupRealize(product.id1c)) R.drawable.ic_group_active
                else R.drawable.ic_group
            )

            binding.imgBtnLike.setOnClickListener {
                val isFavorite = btnLikeRealize(product.id1c)
                binding.imgBtnLike.setImageResource(
                    if (!isFavorite) R.drawable.ic_heath_active
                    else R.drawable.heart_passive
                )
                clickFavorite(!isFavorite, product.id1c)
            }

            binding.imgBtnGroup.setOnClickListener {
                val isGroup = btnGroupRealize(product.id1c)
                binding.imgBtnGroup.setImageResource(
                    if (!isGroup) R.drawable.ic_group_active
                    else R.drawable.ic_group
                )
                clickGroup(!isGroup, product.id1c)
            }

            binding.root.setOnClickListener {
                clickToCard(product)
            }

            binding.tvCountProducts.text = "Наличие: ${product.count} шт"

            // Initialize countProducts with value from the basket
            var countProducts = cart.products.find { it.prodId == product.id1c }?.count ?: 0
            binding.tvCountMy.text = countProducts.toString()
            updateBasketVisibility(binding, countProducts, product.id1c)

            binding.btnMinus.setOnClickListener {
                if (countProducts > 0) {
                    countProducts--
                    binding.tvCountMy.text = countProducts.toString()
                    updateBasketVisibility(binding, countProducts, product.id1c)

                    if (countProducts == 0){
                        Log.d("Mylog", "Id btn minus = ${product.title}")
                        cart.products.forEach {
                            if (it.prodId == product.id1c) {
                                clickDeleteBasket(it.id)
                                return@forEach
                            }
                        }
                    }
                }
            }

            binding.btnPlus.setOnClickListener {
                if (countProducts < product.count) {
                    countProducts++
                    updateBasketVisibility(binding, countProducts, product.id1c)
                    binding.tvCountMy.text = countProducts.toString()
                }
            }

            binding.btnBasket.setOnClickListener {
                countProducts++
                updateBasketVisibility(binding, countProducts, product.id1c)
                binding.tvCountMy.text = countProducts.toString()
            }
        }
    }

    private fun updateBasketVisibility(
        binding: ProductViewGroupBinding,
        count: Int,
        prodId: String
    ) {
        if (count > 0) {
            binding.btnBasket.visibility = View.GONE
            binding.consLayoutBasketActive.visibility = View.VISIBLE
            clickEventBasket(prodId, count)
        } else {
            binding.btnBasket.visibility = View.VISIBLE
            binding.consLayoutBasketActive.visibility = View.GONE
        }
    }

    private fun btnLikeRealize(id1c: String): Boolean {
        return wishList.any { it.prodId == id1c }
    }

    private fun btnGroupRealize(id1c: String): Boolean {
        return compareList.any { it.prodId == id1c }
    }

    fun updateLists(newCatalog: List<Product>, newWishList: List<WishListCompareMini>, newCompareList: List<WishListCompareMini>, newCart: CartMini) {
        wishList = newWishList
        compareList = newCompareList
        cart = newCart
        submitList(newCatalog)
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id1c == newItem.id1c
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.title == newItem.title
    }
}



