package com.example.bio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bio.data.dto.CategoriesFindAllDto
import com.example.bio.databinding.CategoryListItemBinding
import com.example.bio.domain.entities.CategoriesFindAll

class CategoriesAdapter(
    private var list: List<CategoriesFindAll>,
    private val clickRoot: (slug: String) -> Unit
) : ListAdapter<CategoriesFindAll, CategoriesAdapter.CategoryListViewHolder>(CategoriesDiffCallback()) {

    private var parentSlug = "index"

    val ALL_ITEM = CategoriesFindAllDto(
        id = -1,
        id1c = "header",
        status = 1,
        slug = "index",
        title = "Все товары",
        text = null,
        photo = null,
        popular = 0,
        parentId = null,
        discountId = null,
        discount = null,
        childCategory = emptyList()
    )


    inner class CategoryListViewHolder(private val binding: CategoryListItemBinding) :
        ViewHolder(binding.root) {

        fun bind(category: CategoriesFindAll) = with(binding) {
            tvCategory.text = category.title

            root.setOnClickListener {
                if (category.childCategory.isNotEmpty()) {
                    Log.d("Mylog", "Update list ---- true = ${category.childCategory}")
                    parentSlug = category.slug
                    updateLists(category.childCategory)
                } else {
                    Log.d("Mylog", "Click list --- false")
                    clickRoot(category.slug)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        Log.d("Mylog", "On create view holder adapter --- categories")
        val binding = CategoryListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val data = list[position]
        Log.d("Mylog", "On bind view holder $data ")
        holder.bind(data)
    }

    fun updateLists(newList: List<CategoriesFindAll>) {
        Log.d("Mylog", "update list size = ${newList.size}")
        val updatedList = newList.toMutableList().apply {
            add(0, ALL_ITEM.apply { slug = parentSlug })
        }
        list = updatedList
        Log.d("Mylog", "update list size = ${updatedList.size}")
        submitList(updatedList)
    }
}

class CategoriesDiffCallback : DiffUtil.ItemCallback<CategoriesFindAll>() {
    override fun areItemsTheSame(oldItem: CategoriesFindAll, newItem: CategoriesFindAll): Boolean {
        return oldItem.id1c == newItem.id1c
    }

    override fun areContentsTheSame(
        oldItem: CategoriesFindAll,
        newItem: CategoriesFindAll
    ): Boolean {
        return oldItem.title == newItem.title
    }
}