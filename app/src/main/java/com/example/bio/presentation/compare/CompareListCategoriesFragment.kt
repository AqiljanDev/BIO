package com.example.bio.presentation.compare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.databinding.FragmentCompareImplBinding
import com.example.bio.databinding.FragmentCompareListCategoriesBinding
import com.example.bio.domain.entities.compare.ProductWrapper
import com.example.bio.presentation.adapter.CompareCategoriesAdapter
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CompareListCategoriesFragment : Fragment() {

    private val viewModel: CompareViewModel by viewModels()
    private val binding: FragmentCompareListCategoriesBinding by lazy {
        FragmentCompareListCategoriesBinding.inflate(layoutInflater)
    }

    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCompares(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))

        viewModel.compareList.onEach { compare ->

            val productsByCategory: Map<String, List<ProductWrapper>> =
                compare.products.groupBy { it.product.categoriesId }



            val adapterCategories = CompareCategoriesAdapter { categoriesId ->
                showCompare(categoriesId)
            }
            binding.rcCategories.adapter = adapterCategories

            adapterCategories.submitList(productsByCategory.values.toMutableList())

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showCompare(categoriesId: String) {
        val bundle = Bundle().apply {
            putString("categoriesId", categoriesId)
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_compare, CompareImplFragment().apply { arguments = bundle })
            .addToBackStack(null)
            .commit()
    }

}