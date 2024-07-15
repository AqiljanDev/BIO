package com.example.bio.presentation.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.databinding.FragmentFilterFullBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.FullFilterCharactersAdapter
import com.example.bio.presentation.left_menu.CategoriesListFragment
import com.example.bio.presentation.search.SearchViewModel
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFullFragment : Fragment(), SortSheetFragment.BottomSheetListener {

    private var paramSort: String = ""
    private var paramCategory: String = ""
    private var paramActiveCharacters: String = ""

    private val binding: FragmentFilterFullBinding by lazy {
        FragmentFilterFullBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchViewModel by viewModels()

    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramSort = it.getString("sort") ?: "new"
            paramCategory = it.getString("category") ?: "index"
            paramActiveCharacters = it.getString("characters") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        // Fetch data asynchronously
        lifecycleScope.launch {
            val token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)
            viewModel.getCharacters(token, paramCategory)
        }

        binding.tvSortSub.text = FilterFragment.SORT.entries.first { it.message == paramSort }.translate

        setFragmentResultListener("category_list") { _, bundle ->
            bundle.getString("category")?.let { paramCategory = it }
        }

        val adapter = FullFilterCharactersAdapter(paramActiveCharacters.split("."))
        binding.brandRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.characterCategory.collect { characterCategory ->
                adapter.updateList(characterCategory.characters, paramActiveCharacters.split("."))
            }
        }

        setupPriceRangeSlider()
        setupEditTextListeners()

        binding.frameLayoutCategory.setOnClickListener {
            val bundle = Bundle().apply { putBoolean("filter", true) }
            (activity as MainActivity).replacerFragment(
                CategoriesListFragment().apply { arguments = bundle }
            )
        }

        binding.frameLayoutSort.setOnClickListener {
            val sortSheetFragment = SortSheetFragment.newInstance()
            sortSheetFragment.setTargetFragment(this, 0)
            sortSheetFragment.show(parentFragmentManager, sortSheetFragment.tag)
        }
    }

    private fun setupPriceRangeSlider() {
        binding.priceRangeSlider.apply {
            valueFrom = 0f
            valueTo = 1000000f
            stepSize = 1f
            setValues(0f, 1000000f)
        }

        binding.priceRangeSlider.addOnChangeListener { slider, _, _ ->
            binding.minPriceEditText.setText(slider.values[0].toInt().toString())
            binding.maxPriceEditText.setText(slider.values[1].toInt().toString())
        }
    }

    private fun setupEditTextListeners() {
        binding.minPriceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val minValue = s.toString().toIntOrNull() ?: 0
                val maxValue = binding.maxPriceEditText.text.toString().toIntOrNull() ?: 1_000_000
                if (minValue <= maxValue) {
                    binding.priceRangeSlider.setValues(minValue.toFloat(), maxValue.toFloat())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.maxPriceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val maxValue = s.toString().toIntOrNull() ?: 1_000_000
                val minValue = binding.minPriceEditText.text.toString().toIntOrNull() ?: 0
                if (minValue <= maxValue) {
                    binding.priceRangeSlider.setValues(minValue.toFloat(), maxValue.toFloat())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val bundle = Bundle().apply {
            putString("sort", "Otvetka == $paramSort")
            putString("category", "Otvetka == $paramCategory")
            putString("characters", "Otvetka == $paramActiveCharacters")
            putInt("price_min", 0)
            putInt("price_max", 10_000)
        }

        setFragmentResult("result", bundle)
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onSortSelected(sort: FilterFragment.SORT) {
        paramSort = sort.message
    }

    override fun getCurrentSort(): FilterFragment.SORT {
        return FilterFragment.SORT.entries.first { it.message == paramSort }
    }
}
