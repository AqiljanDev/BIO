package com.example.bio.presentation.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.databinding.FragmentFilterFullBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.FullFilterCharactersAdapter
import com.example.bio.presentation.left_menu.CategoriesListFragment
import com.example.bio.presentation.search.SearchViewModel
import com.example.bio.utils.toggleItem
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFullFragment : Fragment(), SortSheetFragment.BottomSheetListener {
    private var cansel = false

    private var paramSort: String = ""
    private var paramCategory: String = ""
    private var paramActiveCharacters: MutableList<String> = mutableListOf()

    private val binding: FragmentFilterFullBinding by lazy {
        FragmentFilterFullBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchViewModel by activityViewModels()

    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { res ->
            paramSort = res.getString("sort") ?: "new"
            paramCategory = res.getString("category") ?: "index"
            val res = res.getString("characters") ?: ""
            paramActiveCharacters = res.split(".").filter { it.isNotEmpty() }.toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        Log.d("log", "Param char active = $paramActiveCharacters")

        getCategory()

        binding.tvSortSub.text =
            FilterFragment.SORT.entries.first { it.message == paramSort }.translate

        setFragmentResultListener("category_list") { _, bundle ->
            bundle.getString("category")?.let { paramCategory = it }
            getCategory()
        }

        val adapter = FullFilterCharactersAdapter(paramActiveCharacters) {
            paramActiveCharacters.toggleItem(it)
        }
        binding.brandRecyclerView.adapter = adapter

        viewModel.characterCategory.onEach { characterCategory ->
            adapter.updateList(characterCategory.characters, paramActiveCharacters)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

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

        binding.tvCancel.setOnClickListener {
            cansel = true
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnShow.setOnClickListener {
            cansel = false
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvReset.setOnClickListener {
            paramSort = FilterFragment.SORT.NEW.message
            binding.tvSortSub.text = FilterFragment.SORT.NEW.translate

            binding.minPriceEditText.text.clear()
            binding.maxPriceEditText.text.clear()

            paramCategory = "index"
            paramActiveCharacters.clear()

            getCategory()
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                // Ваша функция, которая должна быть вызвана после обновления данных в адаптере
                onAdapterDataChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                // Ваша функция, которая должна быть вызвана после изменения диапазона элементов в адаптере
                onAdapterDataChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                // Ваша функция, которая должна быть вызвана после вставки диапазона элементов в адаптере
                onAdapterDataChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                // Ваша функция, которая должна быть вызвана после удаления диапазона элементов в адаптере
                onAdapterDataChanged()
            }
        })
    }

    private fun onAdapterDataChanged() {
        binding.frameLayoutProgressBar.visibility = View.GONE
        Log.d("Mylog", "Adapter data has changed.")
    }

    private fun getCategory() {
        lifecycleScope.launch {
            val token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)
            viewModel.getCharacters(token, paramCategory)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("log", "Cancenl bool = $cansel, paramActiveCharacters: $paramActiveCharacters")
        if (!cansel) {

            val bundle = Bundle().apply {
                putString("sort", paramSort)
                putString("category", paramCategory)
                putString("characters", paramActiveCharacters.filter { it.isNotEmpty() }.joinToString("."))
                putString("price_min", binding.minPriceEditText.text.toString())
                putString("price_max", binding.maxPriceEditText.text.toString())
            }

            setFragmentResult("result", bundle)
        }
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onSortSelected(sort: FilterFragment.SORT) {
        paramSort = sort.message
        binding.tvSortSub.text = sort.translate
    }

    override fun getCurrentSort(): FilterFragment.SORT {
        return FilterFragment.SORT.entries.first { it.message == paramSort }
    }
}
