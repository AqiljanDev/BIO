package com.example.bio.presentation.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
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
import com.example.bio.presentation.adapter.FilterCharactersAdapter
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.Quad
import com.example.bio.presentation.left_menu.CategoriesListFragment
import com.example.bio.presentation.search.SearchViewModel
import com.example.bio.utils.toggleItem
import com.example.bio.utils.toggleItems
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FilterFragment : Fragment(), SheetFragment.BottomSheetListener {

    private val viewModel: SearchViewModel by viewModels()
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
    private var sort: String = ""
    private val listActiveId1c: MutableList<String> = mutableListOf()
    private var currentPage: Int = 1

    private var listCharactersCurrent: List<Character> = listOf()
    private var brandTitle: String = ""

    private lateinit var adapter: CategoryAdapter
    private lateinit var adapterCharacters: CharactersAdapter

    private var listFavorite: MutableList<String> = mutableListOf()
    private var listGroup: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            currentCategory = it.getString("category", "index")
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

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.characterCategory.onEach {
            Log.d("Mylog", "Character category onEach open")
            val list = if (it.characters.size > 9) {
                it.characters.slice(0..9)
            } else it.characters

            adapterCharacters.updateList(list as List<BrandDto>, listActiveId1c)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnImgSort.setOnClickListener {
            val sheetFragment = SheetFragment.newInstance()
            sheetFragment.setTargetFragment(this, 0) // Устанавливаем целевой фрагмент
            sheetFragment.show(parentFragmentManager, sheetFragment.tag)
        }

        binding.btnImgFilter.setOnClickListener {

        }

        realizeCurrentCatalog()
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
        binding.rcFilterProducts.adapter = adapter

        adapterCharacters = CharactersAdapter(listActiveId1c) { brand -> onClickCharacter(brand) }
        binding.rcCategories.adapter = adapterCharacters
    }

    private fun observeViewModel() {

        combine(
            viewModel.catalog,
            viewModel.wishListMini,
            viewModel.compareMini,
            viewModel.cartMini
        ) { catalog, wishList, compareList, cart ->
            Quad(catalog.products, wishList, compareList, cart)
        }.onEach { (catalog, wishList, compareList, cart) ->
            Log.d("Mylog", "Combine oneach quad = ${catalog.size}")
            adapter.updateLists(catalog, wishList, compareList, cart)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    private fun getDataRequest() {
        viewModel.getCatalog(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            currentCategory,
            min,
            max,
            sort,
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
}
