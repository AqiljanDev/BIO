package com.example.bio.presentation.category.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bio.data.dto.ProductDto
import com.example.bio.databinding.ProductViewGroupBinding
import com.example.bio.domain.entities.findOne.Product

class CategoryAdapter(
    private var list: List<Product>
) : RecyclerView.Adapter<ProductViewHolder>() {

    fun submitData(listNow: List<Product>) {
        list = listNow

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = list[position]
        Log.d("Mylog", "CATEGORY BIND HOLDER = ${item.title} = ${list.size}")
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        Log.d("Mylog", " ON CREATE VIEW HOLDER")
        val binding = ProductViewGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ProductViewHolder(private val binding: ProductViewGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(product: Product) = with(binding) {
        tvTitle.text = product.title
        tvApt.text = "apt: ${product.article}"

        if (product.charactersToProducts.isNotEmpty()) {
            val size = product.charactersToProducts.size
            val textViews = listOf(tvCharacterOne, tvCharacterTwo, tvCharacterThree)
            val llChar = listOf(llCharacterOne, llCharacterTwo, llCharacterThree)

            for (i in 0 until minOf(size, textViews.size)) {
                llChar[i].visibility = View.VISIBLE
                textViews[i].text = product.charactersToProducts[i].character.title + ": " + product.charactersToProducts[i].characterValue.title
            }
        }

        tvPriceAction.paintFlags = tvPriceAction.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        if (product.gallery.isNotEmpty()) {
            Glide.with(root.context)
                .load("http://192.168.8.237:4040/img/products/" + product.gallery[0].photo)
                .into(imageViewMain)
        }


    }
}

class DiffUtilCallback() : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(
        oldItem: Product,
        newItem: Product
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Product,
        newItem: Product
    ): Boolean = oldItem.slug == newItem.slug

}