package com.example.bio.presentation.compare

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentCompareBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CompareCharactersAdapter
import com.example.bio.presentation.adapter.CompareElementAdapter
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.CustomCompare
import com.example.data.SharedPreferencesManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.typeOf


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

    private val layoutManager1 by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private val layoutManager2 by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private var compareSize = 1
    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCompares(token)

        val adapterChar = CompareCharactersAdapter(listOf())
        binding.rcCharacters.adapter = adapterChar
        binding.rcCharacters.layoutManager = layoutManager2

        val adapterProduct = CompareElementAdapter(
            products = listOf(),
            clickToEventCompare = { prodId -> eventCompare(prodId) },
            clickToEventBasket = { prodId, count -> eventCart(prodId, count) },
            clickDeleteBasket = { id -> deleteCart(id) },
            clickToCard = { slug -> clickToCard(slug) }
        )
        binding.rcViewPage.adapter = adapterProduct
        binding.rcViewPage.layoutManager = layoutManager1

        val snapHelper1 = GravitySnapHelper(Gravity.START)
        val snapHelper2 = GravitySnapHelper(Gravity.START)
        snapHelper1.attachToRecyclerView(binding.rcViewPage)
        snapHelper2.attachToRecyclerView(binding.rcCharacters)

        // Определяем OnScrollListeners для синхронизации прокрутки
        binding.rcViewPage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isScrolling) {
                    isScrolling = true
                    binding.rcCharacters.scrollBy(dx, dy)
                    isScrolling = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateVisibleItemPositions()
                }
            }
        })

        binding.rcCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isScrolling) {
                    isScrolling = true
                    binding.rcViewPage.scrollBy(dx, dy)
                    isScrolling = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateVisibleItemPositions()
                }
            }
        })

        binding.imageViewRightPager.setOnClickListener {
            scrollTo("plus")
            updateVisibleItemPositions()
        }

        binding.imageViewLeftPager.setOnClickListener {
            scrollTo("minus")
            updateVisibleItemPositions()
        }

        combine(
            viewModel.compareList,
            viewModel.getCartMini
        ) { compare, cartMini ->
            Pair(compare, cartMini)
        }.onEach { (compare, cartMini) ->
            if (compare.products.isEmpty()) {
                with(binding) {
                    tvProductEmpty.visibility = View.VISIBLE
                    rcViewPage.visibility = View.GONE
                    frameLayoutBtnPager.visibility = View.GONE
                    viewLineBottomFrame.visibility = View.GONE
                    tvAllCharacters.visibility = View.GONE
                    rcCharacters.visibility = View.GONE
                }

            } else {
                with(binding) {
                    tvProductEmpty.visibility = View.GONE
                    rcViewPage.visibility = View.VISIBLE
                    frameLayoutBtnPager.visibility = View.VISIBLE
                    viewLineBottomFrame.visibility = View.VISIBLE
                    tvAllCharacters.visibility = View.VISIBLE
                    rcCharacters.visibility = View.VISIBLE
                }
            }

            if (compare.characters.isEmpty()) {
                binding.tvAllCharacters.visibility = View.GONE
                binding.rcCharacters.visibility = View.GONE
            } else {
                binding.tvAllCharacters.visibility = View.VISIBLE
                binding.rcCharacters.visibility = View.VISIBLE
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
                    it.product.characters
                )
            }.toMutableList()

            Log.d("Mylog", "Character 'compare' = $customCompare")
            Log.d("Mylog", "Character product 'custom' size = ${customCompare.size}")
            compareSize = customCompare.size

            // Обновляем адаптер
            adapterProduct.updateLists(
                customCompare,
                cartMini.products
            )

            adapterChar.updateList(
                compare.characters.map { it.title },
                customCompare
            )

            binding.rcViewPage.viewTreeObserver.addOnGlobalLayoutListener {
                updateVisibleItemPositions()
            }

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
        updateVisibleItemPositions()
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

    private fun updateVisibleItemPositions() {
        val firstVisibleItemPosition1 = layoutManager1.findFirstVisibleItemPosition()
        val lastVisibleItemPosition1 = layoutManager1.findLastVisibleItemPosition()
        val firstCompletelyVisibleItemPosition1 =
            layoutManager1.findFirstCompletelyVisibleItemPosition()
        val lastCompletelyVisibleItemPosition1 =
            layoutManager1.findLastCompletelyVisibleItemPosition()

        val firstVisibleItemPosition2 = layoutManager2.findFirstVisibleItemPosition()
        val lastVisibleItemPosition2 = layoutManager2.findLastVisibleItemPosition()
        val firstCompletelyVisibleItemPosition2 =
            layoutManager2.findFirstCompletelyVisibleItemPosition()
        val lastCompletelyVisibleItemPosition2 =
            layoutManager2.findLastCompletelyVisibleItemPosition()

        val itemCount = binding.rcViewPage.adapter?.itemCount ?: 0

        Log.d("Mylog", "compareSize: $compareSize, itemCount1: $itemCount, itemCount2: $itemCount")
        Log.d(
            "Mylog",
            "firstVisibleItemPosition1: $firstVisibleItemPosition1, lastVisibleItemPosition1: $lastVisibleItemPosition1"
        )
        Log.d(
            "Mylog",
            "firstCompletelyVisibleItemPosition1: $firstCompletelyVisibleItemPosition1, lastCompletelyVisibleItemPosition1: $lastCompletelyVisibleItemPosition1"
        )
        Log.d(
            "Mylog",
            "firstVisibleItemPosition2: $firstVisibleItemPosition2, lastVisibleItemPosition2: $lastVisibleItemPosition2"
        )
        Log.d(
            "Mylog",
            "firstCompletelyVisibleItemPosition2: $firstCompletelyVisibleItemPosition2, lastCompletelyVisibleItemPosition2: $lastCompletelyVisibleItemPosition2"
        )

        val startPosition1 = if (firstCompletelyVisibleItemPosition1 != RecyclerView.NO_POSITION) {
            firstCompletelyVisibleItemPosition1
        } else {
            firstVisibleItemPosition1
        }

        val endPosition1 = if (lastCompletelyVisibleItemPosition1 != RecyclerView.NO_POSITION) {
            lastCompletelyVisibleItemPosition1
        } else {
            lastVisibleItemPosition1
        }

        val startPosition2 = if (firstCompletelyVisibleItemPosition2 != RecyclerView.NO_POSITION) {
            firstCompletelyVisibleItemPosition2
        } else {
            firstVisibleItemPosition2
        }

        val endPosition2 = if (lastCompletelyVisibleItemPosition2 != RecyclerView.NO_POSITION) {
            lastCompletelyVisibleItemPosition2
        } else {
            lastVisibleItemPosition2
        }

        if (startPosition1 != RecyclerView.NO_POSITION && endPosition1 != RecyclerView.NO_POSITION) {
            val displayText1 = "${startPosition1 + 1}-${endPosition1 + 1}/$itemCount"
            binding.tvCurrentPager.text = displayText1
            Log.d("Mylog", "ViewPage: $displayText1")
        }

        if (startPosition2 != RecyclerView.NO_POSITION && endPosition2 != RecyclerView.NO_POSITION) {
            val startPositionRes =
                (if (startPosition1 >= startPosition2) startPosition2 else startPosition1) + 1
            var endPositionRes = (if (endPosition1 >= endPosition2) endPosition1 else endPosition2) + 1

            if (startPositionRes == 1 && (itemCount == 3 || itemCount == 2)) endPositionRes = 2

            val displayText2 = "$startPositionRes-$endPositionRes/$itemCount"
            binding.tvCurrentPager.text = displayText2
            Log.d("Mylog", "Characters: $displayText2")
        }
    }

    private fun scrollTo(state: String) {
        val currentPosition = layoutManager1.findFirstVisibleItemPosition()

        Log.d("Mylog", "Current pos = $currentPosition")

        if (currentPosition != RecyclerView.NO_POSITION && currentPosition < compareSize - 1 && state == "plus") {
            Log.d("Mylog", "next pos = ${currentPosition + 2}")
            binding.rcViewPage.smoothScrollToPosition(currentPosition + 2)
        }

        if (currentPosition != RecyclerView.NO_POSITION && currentPosition > 0 && state == "minus") {
            binding.rcViewPage.smoothScrollToPosition(currentPosition - 1)
        }
    }

}




