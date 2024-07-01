package com.example.bio.presentation.category

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.ChildCategoryDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentCategoryBinding
import com.example.bio.domain.entities.findOne.ChildCategory
import com.example.bio.domain.entities.findOne.Info
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.Quad
import com.example.bio.presentation.data.State
import com.example.data.SharedPreferencesManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : BaseBottomFragment() {
    private var request: String = "index"

    private val viewModel: CategoryViewModel by viewModels()
    private val binding: FragmentCategoryBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }
    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token: String by lazy { sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN) }

    private var tagHide: Boolean = true
    private var currentSlugCategory = "Katalog"
    private var currentPage = 1

    private var listFavorite: MutableList<String> = mutableListOf()
    private var listGroup: MutableList<String> = mutableListOf()

    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request = it.getString("category") ?: "index"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentSlugCategory = when {
            request != "index" -> request
            viewModel.saveCategory.value != "index" -> {
                currentPage = viewModel.savePages.value
                viewModel.saveCategory.value
            }

            else -> "index"
        }

        // Запрос на Каталог(все категории)
        viewModel.getCategoryProduct(
            token,
            currentSlugCategory,
            currentPage
        )


        setupAdapter()
        observeViewModel()

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
                token = token,
                category = currentSlugCategory,
                page = page
            )
            currentPage = page
            binding.nestedScrollView.fling(0) // Sets mLastScrollerY for next command
            binding.nestedScrollView.smoothScrollTo(0, 0) // Starts a scroll itself
        }
    }

    private fun setupAdapter() {
        adapter = CategoryAdapter(
            listOf(),
            listOf(),
            listOf(),
            CartMiniDto(emptyList(), 0),
            { isState, id1c -> updateFavorite(isState, id1c) },
            { isState, id1c -> updateGroup(isState, id1c) },
            { prodId, count -> updateBasket(prodId, count) },
            { id -> deleteBasket(id) }
        ) { product ->
            val bundle = Bundle().apply { putString("category", product.slug) }
            (activity as MainActivity).apply {
                replacerFragment(ProductCardFragment().apply { arguments = bundle })
            }
        }
        binding.rcProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.categoryProduct.onEach { categoryProduct ->
            addChips(categoryProduct.info.childCategory)
            initializeChips(categoryProduct.info)

            currentSlugCategory = categoryProduct.info.slug
            binding.tvCurrentType.text = categoryProduct.info.title

            updateAdapterData(categoryProduct.products)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        combine(
            viewModel.categoryProduct,
            viewModel.wishListMini,
            viewModel.compareMini,
            viewModel.cartMini
        ) { catalog, wishList, compareList, cart ->
            Quad(catalog.products, wishList, compareList, cart)
        }.onEach { (catalog, wishList, compareList, cart) ->
            Log.d("Mylog", "Product size = ${catalog.size}")
            if (catalog.isEmpty()) {
                binding.paginationView.visibility = View.GONE
                binding.tvProductsEmpty.visibility = View.VISIBLE
            }else {
                binding.paginationView.visibility = View.VISIBLE
                binding.tvProductsEmpty.visibility = View.GONE
            }
            adapter.updateLists(catalog, wishList, compareList, cart)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateAdapterData(products: List<Product>) {
        adapter.submitList(products)
    }

    private fun updateBasket(prodId: String, count: Int) {
        viewModel.eventCart(token, PostCartDto(prodId, count))
        (activity as MainActivity).badgeBasket.isVisible = true
    }

    private fun deleteBasket(id: Int) {
        try {
            Log.d("Mylog", "ID count = $id")
            viewModel.deleteCart(token, id)
        } catch (ex: Exception) {
            Log.d("Mylog", "Exception === ${ex.message}")
        }
        (activity as MainActivity).badgeBasket.isVisible = false
    }

    private fun updateGroup(state: Boolean, id1c: String) {
        if (state) listGroup.add(id1c) else listGroup.remove(id1c)
        (activity as MainActivity).badgeGroup.isVisible = listGroup.isNotEmpty()
        viewModel.eventCompare(token, id1c)
    }

    private fun updateFavorite(state: Boolean, id1c: String) {
        if (state) listFavorite.add(id1c) else listFavorite.remove(id1c)
        (activity as MainActivity).badgeFavorite.isVisible = listFavorite.isNotEmpty()
        viewModel.eventWishList(token, id1c)
    }

    private fun initializeChips(info: Info) {
        binding.cgHistoryCatalog.removeAllViews()
        var currentCategory = info.parentCategory

        addChipHistoryCategory(info.title, info.slug)

        while (currentCategory != null) {
            addDividerText("/")
            addChipHistoryCategory(currentCategory.title, currentCategory.slug)
            currentCategory = currentCategory.parentCategory
        }
    }

    private fun clickTagCategory(slug: String) {
        viewModel.getCategoryProduct(
            token = token,
            category = slug,
            page = 1
        )
        currentPage = 1
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


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Mylog", "On destroy slug: $currentSlugCategory")

        viewModel.savesCategory(currentSlugCategory)
        viewModel.savesPage(currentPage)
    }
}
