package com.example.bio.presentation.search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentSearchBinding
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.adapter.SearchHistoryAdapter
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.data.Quad
import com.example.bio.utils.vibratePhone
import com.example.data.SearchHistoryManager
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModels()
    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }
    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token: String by lazy { sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN) }

    private lateinit var searchHistoryManager: SearchHistoryManager
    private lateinit var adapterSearchHistory: SearchHistoryAdapter

    private lateinit var adapter: CategoryAdapter

    private var listFavorite: MutableList<String> = mutableListOf()
    private var listGroup: MutableList<String> = mutableListOf()

    private val bottomNavigationView: BottomNavigationView? by lazy {
        activity?.findViewById(R.id.bottom_navigation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        setupAdapter()
        observeViewModel()


        searchHistoryManager = SearchHistoryManager(requireContext())
        if (searchHistoryManager.getHistory().isEmpty()) {
            binding.llHistory.visibility = View.GONE
            binding.rcHistory.visibility = View.GONE
        }

        adapterSearchHistory = SearchHistoryAdapter(searchHistoryManager.getHistory()) { query ->
            binding.editTextSearch.setQuery(query, false)
            performSearch(query)
        }

        binding.rcHistory.adapter = adapterSearchHistory

        binding.editTextSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performSearch(query)
                    binding.rcHistory.visibility = View.GONE
                    binding.llHistory.visibility = View.GONE
                }
                val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    updateHistory()
                    binding.rcHistory.visibility = View.VISIBLE
                    binding.llHistory.visibility = View.VISIBLE
                } else {
                    viewModel.getSearchResults(token, newText)
                    updateHistory()
                    binding.rcHistory.visibility = View.GONE
                    binding.llHistory.visibility = View.GONE
                }
                return true
            }
        })

        binding.tvClearHistory.setOnClickListener {
            searchHistoryManager.clearHistory()
            updateHistory()
        }

        binding.btnCancel.setOnClickListener {
            (activity as MainActivity).replacerFragment(CategoryFragment())
        }
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
        binding.rvSearchProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.listProduct.onEach { products ->
            updateAdapterData(products)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        combine(
            viewModel.listProduct,
            viewModel.wishListMini,
            viewModel.compareMini,
            viewModel.cartMini,
            viewModel.profileDiscount
        ) { catalog, wishList, compareList, cart, profile ->
            Quad(catalog, wishList, compareList, cart, profile)
        }.onEach { (catalog, wishList, compareList, cart, profile) ->
            adapter.updateLists(catalog, wishList, compareList, cart, profile)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateAdapterData(products: List<Product>) {
        adapter.submitList(products)
    }

    private fun updateBasket(prodId: String, count: Int) {
        viewModel.eventCart(token, PostCartDto(prodId, count))
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
        if (state) listFavorite.add(id1c) else listFavorite.remove(id1c)
        (activity as MainActivity).badgeFavorite.isVisible = listFavorite.isNotEmpty()

        if (listFavorite.isNotEmpty()) requireContext().vibratePhone()

        viewModel.eventWishList(token, id1c)
    }

    private fun performSearch(query: String) {
        Log.d("Mylog", "Viewmodel search == $query")
        searchHistoryManager.saveQuery(query)
        viewModel.getSearchResults(token, query)
        updateHistory()
    }

    private fun updateHistory() {
        val history = searchHistoryManager.getHistory()
        adapterSearchHistory = SearchHistoryAdapter(history) { query ->
            binding.editTextSearch.setQuery(query, false)
            performSearch(query)
        }
        binding.rcHistory.adapter = adapterSearchHistory
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}
