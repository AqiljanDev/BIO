package com.example.bio.presentation.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.data.dto.WishListFullDto
import com.example.bio.databinding.FragmentFavoriteBinding
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.Quad
import com.example.bio.utils.vibratePhone
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FavoriteFragment : BaseBottomFragment() {

    private val viewModel: FavoriteViewModel by viewModels()
    private val binding: FragmentFavoriteBinding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: CategoryAdapter // Используйте свой адаптер здесь

    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token: String by lazy { sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN) }

    private var listGroup: MutableList<String> = mutableListOf()

    private val bottomNavigationView: BottomNavigationView? by lazy {
        activity?.findViewById(R.id.bottom_navigation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts(token)
        setupAdapter()
        observeViewModel()
    }

    private fun setupAdapter() {
        adapter = CategoryAdapter(
            listOf(), // Передайте пустой список, если данных еще нет
            listOf(), // Пустой список для списка избранных, если еще нет данных
            listOf(), // Пустой список для списка сравнения, если еще нет данных
            CartMiniDto(emptyList(), 0), // Пустой CartMini, если еще нет данных
            listOf(),
            { isState, id1c -> updateFavorite(isState, id1c) },
            { isState, id1c -> updateGroup(isState, id1c) },
            { prodId, count -> updateBasket(prodId, count) },
            { id -> deleteBasket(id) },
            { product -> navigateToProductCard(product) }
        )

        binding.rvFavoriteProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.fullProductList.onEach { products ->
            updateAdapterData(products)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        combine(
            viewModel.fullProductList,
            viewModel.wishListMini,
            viewModel.compareMini,
            viewModel.cartMini,
            viewModel.profileDiscount
        ) { catalog, wishList, compareList, cart, profile ->
            Quad(catalog.map { it.product }, wishList, compareList, cart, profile)
        }.onEach { (catalog, wishList, compareList, cart, profile) ->
            adapter.updateLists(catalog, wishList, compareList, cart, profile)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun updateAdapterData(products: List<WishListFullDto>) {
        val productList = products.map { it.product }
        adapter.submitList(productList)
    }

    private fun updateBasket(prodId: String, count: Int) {
        viewModel.eventCart(
            token,
            PostCartDto(prodId, count)
        )
        (activity as MainActivity).badgeBasket.isVisible = true

        requireContext().vibratePhone()
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
        viewModel.eventWishList(token, id1c)
        viewModel.getProducts(token)
        viewModel.getProducts(token)
    }

    private fun navigateToProductCard(product: Product) {
        val bundle = Bundle().apply { putString("category", product.slug) }
        (activity as MainActivity).replacerFragment(ProductCardFragment().apply {
            arguments = bundle
        })
    }

}
