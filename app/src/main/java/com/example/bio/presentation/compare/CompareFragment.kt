package com.example.bio.presentation.compare

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.R
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentCompareBinding
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.compare.CharactersToCompare
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.compare.ProductCompare
import com.example.bio.domain.entities.compare.ProductWrapper
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CompareCategoriesAdapter
import com.example.bio.presentation.adapter.CompareCharactersAdapter
import com.example.bio.presentation.adapter.CompareElementAdapter
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.data.CategoryGroupFull
import com.example.bio.utils.vibratePhone
import com.example.data.SharedPreferencesManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCompares(token)

        viewModel.compareList.onEach { compare ->

            if (compare.products.isNotEmpty()) {

                binding.tvCompareIsClear.visibility = View.GONE
                binding.fragmentContainerCompare.visibility = View.VISIBLE

                if (savedInstanceState == null) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_compare, CompareListCategoriesFragment())
                        .commit()
                }
            } else {
                binding.fragmentContainerCompare.visibility = View.GONE
                binding.tvCompareIsClear.visibility = View.VISIBLE
            }


        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }


}




