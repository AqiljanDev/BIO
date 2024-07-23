package com.example.bio.presentation.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.data.dto.collectCharacters.BrandDto
import com.example.bio.databinding.FragmentFilterBinding
import com.example.bio.domain.entities.collectCharacters.Brand
import com.example.bio.domain.entities.collectCharacters.Character
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.adapter.CharactersAdapter
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.Quad
import com.example.bio.presentation.data.QuadCatalog
import com.example.bio.presentation.left_menu.CategoriesListFragment
import com.example.bio.presentation.search.SearchViewModel
import com.example.bio.utils.toggleItems
import com.example.bio.utils.vibratePhone
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FilterFragment : Fragment(), SheetFragment.BottomSheetListener, SortSheetFragment.BottomSheetListener {

    private val viewModel: SearchViewModel by activityViewModels()
    private val binding: FragmentFilterBinding by lazy {
        FragmentFilterBinding.inflate(layoutInflater)
    }
    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token: String by lazy {
        sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)
    }

    private var currentCategory = "index"
    private var min: Int? = null
    private var max: Int? = null
    private var sort: SORT = SORT.NEW
    private var listActiveId1c: MutableList<String> = mutableListOf()
    private var currentPage: Int = 1

    private var listCharactersCurrent: List<Character> = listOf()
    private var brandTitle: String = ""

    private lateinit var adapter: CategoryAdapter
    private lateinit var adapterCharacters: CharactersAdapter

    private var listFavorite: MutableList<String> = mutableListOf()
    private var listGroup: MutableList<String> = mutableListOf()

    private val bottomNavigationView: BottomNavigationView? by lazy {
        activity?.findViewById(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            currentCategory = it.getString("category", "index")
        }

        // Установка слушателя результатов
        setFragmentResultListener("result") { requestKey, bundle ->
            val resSort = bundle.getString("sort")
            currentCategory = bundle.getString("category") ?: currentCategory
            val resChar = bundle.getString("characters") ?: listActiveId1c.joinToString { "." }
            min = bundle.getString("price_min")?.toIntOrNull()
            max = bundle.getString("price_max")?.toIntOrNull()

            listActiveId1c = resChar.split(".").toMutableList()
            sort = SORT.entries.first { it.message == resSort }

            Log.d("Mylog", "resChar = $resChar, ===m ${bundle.getString("characters")}")
            getDataRequest()
        }

        setFragmentResultListener("category_list") { _, bundle ->
            bundle.getString("category")?.let {
                currentCategory = it
                getDataRequest()
                Log.d("Mylog", "Category result = $it")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Mylog", "Catalog = $currentCategory")
        getDataRequest()
        setupAdapter()
        observeViewModel()

        viewModel.getCharacters(
            token,
            currentCategory
        )

        viewModel.catalog.onEach {
            binding.tvCategoryCurrent.text = it.info.title
            binding.tvCountProducts.text = "${it.products.size} товаров"
            Log.d("Mylog", "Catalog -- ${it.products.size}")

            if (it.products.isEmpty()) {
                binding.rcFilterProducts.visibility = View.GONE
                binding.tvFilterIsClear.visibility = View.VISIBLE
            } else {
                binding.tvFilterIsClear.visibility = View.GONE
                binding.rcFilterProducts.visibility = View.VISIBLE
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.characterCategory.onEach {

            binding.paginationView.visibility = if (it.pages > 1) View.VISIBLE else View.GONE

            binding.paginationView.updateTotalPages(it.pages)

            Log.d("Mylog", "Character category onEach open")
            val list = if (it.characters.size > 9) {
                it.characters.slice(0..9)
            } else it.characters

            adapterCharacters.updateList(list as List<BrandDto>, listActiveId1c)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.paginationView.onPageSelected = { page ->
            currentPage = page

            getDataRequest()
            viewModel.getCharacters(token, currentCategory)
            binding.nestedScrollView.fling(0) // Sets mLastScrollerY for next command
            binding.nestedScrollView.smoothScrollTo(0, 0) // Starts a scroll itself
        }

        binding.btnImgSort.setOnClickListener {
            val sortSheetFragment = SortSheetFragment.newInstance()
            sortSheetFragment.setTargetFragment(this, 0)
            sortSheetFragment.show(parentFragmentManager, sortSheetFragment.tag)
        }

        binding.btnImgFilter.setOnClickListener {
            val bundle = Bundle().apply {
                putString("sort", sort.message)
                putString("category", currentCategory)
                putString("characters", listActiveId1c.joinToString( separator = "." ))
            }

            (activity as MainActivity).replacerFragment(
                FilterFullFragment().apply { arguments = bundle }
            )
        }

        realizeCurrentCatalog()
    }

    private fun setupAdapter() {
        adapter = CategoryAdapter(
            listOf(),
            listOf(),
            listOf(),
            CartMiniDto(emptyList(), 0),
            listOf(),
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
        binding.rcFilterProducts.adapter = adapter

        adapterCharacters = CharactersAdapter(listActiveId1c) { brand -> onClickCharacter(brand) }
        binding.rcCategories.adapter = adapterCharacters
    }

    private fun observeViewModel() {

        combine(
            viewModel.catalog,
            viewModel.wishListMini,
            viewModel.compareMini,
            viewModel.cartMini,
            viewModel.profileDiscount
        ) { catalog, wishList, compareList, cart, profile ->
            QuadCatalog(catalog, wishList, compareList, cart, profile)
        }.onEach { (catalog, wishList, compareList, cart, profile) ->
            Log.d("Mylog", "Combine oneach quad = ${catalog.info.title} + ${catalog.products.size}")
            binding.tvCategoryCurrent.text = catalog.info.title
            adapter.updateLists(catalog.products, wishList, compareList, cart, profile)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateBasket(prodId: String, count: Int) {
        viewModel.eventCart(token, PostCartDto(prodId, count))

        requireContext().vibratePhone()

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

        if (listGroup.isNotEmpty()) requireContext().vibratePhone()

        viewModel.eventCompare(token, id1c)
    }

    private fun updateFavorite(state: Boolean, id1c: String) {
        if (state) listFavorite.add(id1c) else listFavorite.remove(id1c)
        (activity as MainActivity).badgeFavorite.isVisible = listFavorite.isNotEmpty()

        if (listFavorite.isNotEmpty()) requireContext().vibratePhone()

        viewModel.eventWishList(token, id1c)
    }

    private fun getDataRequest() {
        viewModel.getCatalog(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            currentCategory,
            min,
            max,
            sort.message,
            listActiveId1c.joinToString(separator = "."),
            currentPage
        )
    }

    private fun realizeCurrentCatalog() {
        binding.tvCategoryCurrent.setOnClickListener {
            val bundle = Bundle().apply { putBoolean("filter", true) }
            (activity as MainActivity).replacerFragment(
                CategoriesListFragment().apply { arguments = bundle }
            )
        }
    }

    private fun onClickCharacter(brand: Brand) {
        listCharactersCurrent = brand.characters
        brandTitle = brand.title

        val sheetFragment = SheetFragment.newInstance()
        sheetFragment.setTargetFragment(this, 0) // Устанавливаем целевой фрагмент
        sheetFragment.show(parentFragmentManager, sheetFragment.tag)
    }

    override fun onApplyClicked(char: List<String>) {
        listActiveId1c.toggleItems(char)
        adapterCharacters.updateList(adapterCharacters.currentList, listActiveId1c)
        getDataRequest()
    }

    override fun onCharactersList(): List<Character> {
        return listCharactersCurrent
    }

    override fun getTitleBrand(): String {
        return brandTitle
    }

    override fun getId1cActiveList(): MutableList<String> {
        return listActiveId1c
    }

    override fun onSortSelected(sort: SORT) {
        this.sort = sort
        getDataRequest()
    }

    override fun getCurrentSort(): SORT {
        return sort
    }

    enum class SORT(val message: String, val translate: String) {
        NEW(message = "new", translate = "Новые"),
        OLD(message = "old", translate = "Старые"),
        MIN(message = "min", translate = "Сначала дешевые"),
        MAX(message = "max", translate = "Сначала дорогие")
    }
}
