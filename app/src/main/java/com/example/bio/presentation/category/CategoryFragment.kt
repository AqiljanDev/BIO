package com.example.bio.presentation.category

import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.ChildCategoryDto
import com.example.bio.databinding.FragmentCategoryBinding
import com.example.bio.domain.entities.findOne.ChildCategory
import com.example.bio.domain.entities.findOne.Info
import com.example.bio.presentation.category.adapter.CategoryAdapter
import com.example.bio.presentation.category.data.State
import com.example.data.SharedPreferencesManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels()
    private val binding: FragmentCategoryBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }
    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    private var tagHide: Boolean = true
    private var currentSlugCategory = "Katalog"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Запрос на Каталог(все категории)
        viewModel.getCategoryProduct(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN), "index", 1
        )

        viewModel.categoryProduct.onEach {
            addChips(it.info.childCategory)
            initializeChips(it.info)

            currentSlugCategory = it.info.slug
            binding.tvCurrentType.text = it.info.title

            binding.rcProducts.adapter = CategoryAdapter(it.products)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // Номермерное пагинация
        viewModel.countPages.onEach {
            binding.paginationView.updateTotalPages(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // Отслеживание статуса для Progress bar
        viewModel.state.onEach {

            when (it) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is State.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.paginationView.visibility = View.VISIBLE
                }

                is State.Failed -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Ошибка: ${it.message}", Snackbar.LENGTH_LONG)
                        .show()
                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)


        // Функция для обработки клика на номерную пагинацию
        binding.paginationView.onPageSelected = { page ->

            viewModel.getCategoryProduct(
                token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
                category = currentSlugCategory,
                page = page
            )
            binding.nestedScrollView.fling(0);  // Sets mLastScrollerY for next command
            binding.nestedScrollView.smoothScrollTo(0, 0);  // Starts a scroll itself
        }

    }

    private fun initializeChips(info: Info) {
        binding.cgHistoryCatalog.removeAllViews()
        var currentCategory = info.parentCategory

        addChipHistoryCategory(info.title, info.slug)

        while(currentCategory != null) {
            addDividerText("/")
            addChipHistoryCategory(currentCategory.title, currentCategory.slug)
            currentCategory = currentCategory.parentCategory
        }
    }

    private fun clickTagCategory(slug: String) {
        viewModel.getCategoryProduct(
            token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            category = slug,
            page = 1
        )

        binding.paginationView.resetCurrentPage()
    }

    private fun addChips(labels: List<ChildCategory>) {
        val listLabels = addHideOrShow(labels)
        binding.chipGroup.removeAllViews()

        for (label in listLabels) {
            val chip = Chip(context).apply {
                text = label.title
                setChipBackgroundColorResource(R.color.chip_background_color)
                setTextColor(Color.BLACK)
                chipStartPadding = 0f
                chipEndPadding = 0f

                setEnsureMinTouchTargetSize(false)
                chipStrokeWidth = 0f
                isClickable = true
                isCheckable = false

                if (label.slug == "raskryt" || label.slug == "skryt") {
                    setChipBackgroundColorResource(R.color.button_pressed_color)
                    setTextColor(Color.WHITE)
                }

                setOnClickListener {

                    if (label.slug == "raskryt" || label.slug == "skryt") {
                        tagHide = !tagHide
                        addChips(labels)
                    } else {
                        clickTagCategory(label.slug)
                    }
                }

            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun addHideOrShow(list: List<ChildCategory>): List<ChildCategory> {
        return when {
            list.size <= 4 -> list
            tagHide -> {
                list.slice(0..3)
                    .toMutableList()
                    .plus(ChildCategoryDto("Раскрыть", "raskryt"))
            }

            else -> {
                list.toMutableList().plus(ChildCategoryDto("Скрыть", "skryt"))
            }
        }
    }

    private fun addChipHistoryCategory(text: String, slug: String) {

        val chip = Chip(context).apply {
            this.text = text
            textSize = 16f
            setEnsureMinTouchTargetSize(false)

            chipStartPadding = 0f
            chipEndPadding = 0f
            chipStrokeWidth = 0f

            setOnClickListener {
                clickTagCategory(slug)
            }
        }
        binding.cgHistoryCatalog.addView(chip, 0)
    }

    private fun addDividerText(text: String) {
        val textView = TextView(context).apply {
            this.text = text
            textSize = 22f

            gravity = Gravity.CENTER
        }
        binding.cgHistoryCatalog.addView(textView, 0)
    }

}