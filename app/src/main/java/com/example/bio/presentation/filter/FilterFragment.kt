package com.example.bio.presentation.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.databinding.FragmentFilterBinding
import com.example.bio.domain.entities.collectCharacters.Brand
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.adapter.CharactersAdapter
import com.example.bio.presentation.card.ProductCardFragment
import com.example.bio.presentation.search.SearchViewModel
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FilterFragment : Fragment() {
    private var request: String? = ""

    private val viewModel: SearchViewModel by viewModels()
    private val binding: FragmentFilterBinding by lazy {
        FragmentFilterBinding.inflate(layoutInflater)
    }

    private val bottomSheet by lazy {
        activity?.findViewById<LinearLayout>(R.id.bottom_sheet)?.let {
            Log.d("Mylog", "Bottom sheet by lazy, it = $it")
            BottomSheetBehavior.from(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request = it.getString("search")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = SharedPreferencesManager.getInstance(requireContext())

        viewModel.getSearchResults(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            request.toString()
        )

        viewModel.getCharacters(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            "index"
        )

        binding.editTextSearch.setText(request)

        viewModel.listProduct.onEach {
            Log.d("Mylog", "On each active, ${it.size}")

//            binding.rcFilterProducts.adapter = CategoryAdapter(it) { product ->
//                val bundle = Bundle().apply { putString("search", product.slug) }
//                (activity as MainActivity).apply {
//                    replacerFragment( ProductCardFragment().apply { arguments = bundle } )
//                }
//            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.characterCategory.onEach {
            Log.d("Mylog", "Character category onEach open")
            binding.rcCategories.adapter =
                CharactersAdapter(it.characters.slice(0..9)) { brand -> onClickCharacter(brand) }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        Toast.makeText(context, "Argument = $request", Toast.LENGTH_SHORT).show()

        binding.btnImgSort.setOnClickListener {
            bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.btnImgFilter.setOnClickListener {
            Log.d(
                "Mylog",
                "Click filter = sheet: $bottomSheet, activity: $activity, findViewById: ${
                    activity?.findViewById<LinearLayout>(R.id.bottom_sheet)
                }"
            )
            bottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheet?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // BottomSheet открыт - скрываем BottomNavigation
                    (activity as MainActivity)?.hideBottomNavigation()
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    // BottomSheet закрыт - показываем BottomNavigation
                    (activity as MainActivity)?.showBottomNavigation()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Не используется, но метод необходим для реализации интерфейса
            }
        })
    }


    fun onClickCharacter(brand: Brand) {
        bottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED

    }

}