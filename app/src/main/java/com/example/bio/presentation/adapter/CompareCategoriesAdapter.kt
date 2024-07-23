package com.example.bio.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.data.dto.compare.ProductWrapperDto
import com.example.bio.databinding.CompareCategoryItemBinding
import com.example.bio.domain.entities.compare.ProductWrapper

class CompareCategoriesAdapter(
    private val onClickRoot: (categoriesId: String) -> Unit
) : ListAdapter<List<ProductWrapper>, CompareCategoriesAdapter.CompareCategoriesViewHolder>(DiffCallBack()) {

    init {
        submitList(currentList)
    }

    inner class CompareCategoriesViewHolder(private val binding: CompareCategoryItemBinding): ViewHolder(binding.root) {

        fun bind(product: List<ProductWrapper>) = with(binding) {
            tvCategoryTitle.text = product[0].product.categories.parentCategory?.title ?: "Каталог"
            tvCategoryCount.text = product.size.toString()

            root.setOnClickListener {
                onClickRoot(product[0].product.categoriesId)
            }
        }
    }


    class DiffCallBack : DiffUtil.ItemCallback<List<ProductWrapper>>() {
        override fun areItemsTheSame(oldItem: List<ProductWrapper>, newItem: List<ProductWrapper>): Boolean {
            return oldItem[0].id == newItem[0].id
        }

        override fun areContentsTheSame(oldItem: List<ProductWrapper>, newItem: List<ProductWrapper>): Boolean {
            return oldItem as ProductWrapperDto == newItem as ProductWrapperDto
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompareCategoriesViewHolder {
        val view = CompareCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
             false
        )
        return CompareCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompareCategoriesViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }
}