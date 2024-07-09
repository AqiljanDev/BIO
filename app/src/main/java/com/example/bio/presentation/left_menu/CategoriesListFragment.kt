package com.example.bio.presentation.left_menu

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.databinding.FragmentCategoriesListBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoriesAdapter
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.filter.FilterFragment
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {
    private var filter = false

    private val viewModel: CategoriesListViewModel by viewModels()
    private val binding: FragmentCategoriesListBinding by lazy {
        FragmentCategoriesListBinding.inflate(layoutInflater)
    }

    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            filter = it.getBoolean("filter", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CategoriesAdapter(
            list = listOf(),
            clickRoot = { slug -> clickRoot(slug) },
        )
        binding.rcCategories.adapter = adapter

        viewModel.getList(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))

        viewModel.categoryList.onEach {
            Log.d("Mylog", "Category list onEach = $it")


            Log.d("Mylog", "Child category list size: ${it[0].childCategory.size}")

            adapter.updateLists(it[0].childCategory)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun clickRoot(slug: String) {
        Log.d("Mylog", "Click root fragment= $slug, $filter")
        val screen: Fragment = if (filter) FilterFragment() else CategoryFragment()

        val bundle = Bundle().apply { putString("category", slug) }
        (activity as MainActivity).replacerFragment(screen.apply {
            arguments = bundle
        })
    }

}