package com.example.bio.presentation.search

import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentSearchBinding
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.Quad
import com.example.bio.presentation.filter.FilterFragment
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.example.bio.R
import com.example.data.SearchHistoryManager

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }
    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token: String by lazy { sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN) }

    private var request = ""
    private val searchHistoryManager: SearchHistoryManager by lazy {
        SearchHistoryManager(requireContext())
    }

    private lateinit var adapter: CategoryAdapter
    private lateinit var suggestionAdapter: SimpleCursorAdapter

    private var listFavorite: MutableList<String> = mutableListOf()
    private var listGroup: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        setupAdapter()
        observeViewModel()
        setupSearchView()

        binding.btnCancel.setOnClickListener {
            (activity as MainActivity).replacerFragment(CategoryFragment())
        }
    }

    private fun setupSearchView() {
        // Установите `CursorAdapter` для `SearchView`
        val columns = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val suggestionCursor = getSearchSuggestionCursor()

        suggestionAdapter = SimpleCursorAdapter(
            requireContext(),
            R.layout.item_search_history,  // Используем кастомный макет
            suggestionCursor,
            columns,
            intArrayOf(R.id.search_history_text), // Указываем ID для TextView
            0
        )

        val searchView = binding.editTextSearch
        searchView.suggestionsAdapter = suggestionAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchHistoryManager.saveSearchQuery(it)
                    displaySearchResults(it)
                    showSearchHistory()  // Обновите историю после поиска
                    Log.d("Mylog", "On query text submit = $it")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showSearchHistory()
                }
                return true
            }
        })

        // Убедитесь, что вы отображаете историю поиска при открытии SearchView
        searchView.setOnSearchClickListener {
            showSearchHistory()
        }
    }

    private fun getSearchSuggestionCursor(): Cursor {
        val recentSearches = searchHistoryManager.getSearchHistory()
        val matrixCursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

        for ((index, query) in recentSearches.withIndex()) {
            matrixCursor.addRow(arrayOf(index, query))
        }

        return matrixCursor
    }

    private fun displaySearchResults(query: String) {
        // Логика для отображения результатов поиска
    }

    private fun showSearchHistory() {
        val suggestionCursor = getSearchSuggestionCursor()
        suggestionAdapter.changeCursor(suggestionCursor)
        suggestionAdapter.notifyDataSetChanged()
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
            viewModel.cartMini
        ) { catalog, wishList, compareList, cart ->
            Quad(catalog, wishList, compareList, cart)
        }.onEach { (catalog, wishList, compareList, cart) ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}
