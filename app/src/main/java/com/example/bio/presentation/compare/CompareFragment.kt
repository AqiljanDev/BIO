package com.example.bio.presentation.compare

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentCompareBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CompareElementAdapter
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.CustomCompare
import com.example.bio.presentation.data.Duo
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CompareFragment : BaseBottomFragment() {

    private val binding: FragmentCompareBinding by lazy {
        FragmentCompareBinding.inflate(layoutInflater)
    }
    private val viewModel: CompareViewModel by viewModels()

    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    private val token by lazy {
        sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCompares(token)

        val adapter = CompareElementAdapter(
            compareList = mutableListOf(),
            products = listOf(),
            clickToEventCompare = { prodId -> eventCompare(prodId) },
            clickToEventBasket = { prodId, count -> eventCart(prodId, count) },
            clickDeleteBasket = { id -> deleteCart(id) },
            clickToCard = { slug -> clickToCard(slug) }
        )
        binding.rcItems.adapter = adapter

        combine(
            viewModel.compareList,
            viewModel.getCartMini
        ) { compare, cartMini ->
            Pair(compare, cartMini)
        }.onEach { (compare, cartMini) ->
            if (compare.products.isEmpty()) {
                binding.rcItems.visibility = View.GONE
                binding.tvProductEmpty.visibility = View.VISIBLE
            } else {
                binding.rcItems.visibility = View.VISIBLE
                binding.tvProductEmpty.visibility = View.GONE
            }

            // Обновляем customCompare с новыми характеристиками
            val customCompare = compare.products.map {
                CustomCompare(
                    it.id,
                    it.product.id1c,
                    it.product.title,
                    it.product.slug,
                    it.product.photo,
                    it.product.count,
                    it.product.price,
                    if (compare.characters.isNotEmpty()) it.product.characters else emptyList()
                )
            }.toMutableList().apply {
                if (compare.characters.isNotEmpty()) {
                    add(0, CustomCompare(characters = compare.characters.map { it.title }))
                }
            }

            // Обновляем адаптер
            adapter.updateLists(
                customCompare,
                cartMini.products
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun eventCart(prodId: String, count: Int) {
        viewModel.postCart(
            token,
            PostCartDto(prodId, count)
        )
    }

    private fun deleteCart(id: Int) {
        viewModel.deleteCart(
            token,
            id
        )
    }

    private fun clickToCard(slug: String) {
        val bundle = Bundle().apply { putString("category", slug) }
        (activity as MainActivity).replacerFragment(
            ProductCardFragment().apply {
                arguments = bundle
            }
        )
    }

    private fun eventCompare(prodId: String) {
        viewModel.postCompare(token, prodId)
    }
}
