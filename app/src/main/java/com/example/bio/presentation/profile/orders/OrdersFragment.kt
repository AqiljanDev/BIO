package com.example.bio.presentation.profile.orders

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.databinding.FragmentOrdersBinding
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.OrdersHistoryAdapter
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.profile.orders.card.OrderHistoryCardFragment
import com.example.data.SharedPreferencesManager
import com.example.login.ui.main.passwordRestore.PasswordRestoreFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private val viewModel: OrdersViewModel by viewModels()
    private val binding: FragmentOrdersBinding by lazy {
        FragmentOrdersBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }

    private lateinit var adapter: OrdersHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllRequest(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))
        adapter = OrdersHistoryAdapter {
            if (savedInstanceState == null) {
                val bundle = Bundle().apply { putInt("id", it) }
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        OrderHistoryCardFragment().apply { arguments = bundle }
                    ).commit()
            }

        }
        binding.rcHistoryOrders.adapter = adapter

        combine(
            viewModel.profileDiscount,
            viewModel.ordersFindMy
        ) { profileDiscount, ordersFindMy ->
            Pair(profileDiscount, ordersFindMy)
        }.onEach { (profileDiscount, ordersFindMy) -> with(binding) {
            Log.d(
                "Mylog",
                "OrderFragment: Discount.size = ${profileDiscount.size}, Orders.size = ${ordersFindMy.size}"
            )

            if (profileDiscount.isEmpty()) {
                tvHistory.visibility = View.GONE
                rcHistoryOrders.visibility = View.GONE
            } else {
                tvHistory.visibility = View.VISIBLE
                rcHistoryOrders.visibility = View.VISIBLE
            }

            if (profileDiscount.isEmpty()) {
                tvMyDiscount.visibility = View.GONE
                chipGroup.visibility = View.GONE
            } else {
                tvMyDiscount.visibility = View.VISIBLE
                chipGroup.visibility = View.VISIBLE
            }

            if (profileDiscount.isEmpty() && ordersFindMy.isEmpty()) {
                tvMyDiscount.visibility = View.GONE
                tvHistory.visibility = View.GONE
                rcHistoryOrders.visibility = View.GONE
                chipGroup.visibility = View.GONE

                tvNotOrders.visibility = View.VISIBLE
            }else {
                tvMyDiscount.visibility = View.VISIBLE
                tvHistory.visibility = View.VISIBLE
                rcHistoryOrders.visibility = View.VISIBLE
                chipGroup.visibility = View.VISIBLE

                tvNotOrders.visibility = View.GONE
            }

            realizeChipGroup(profileDiscount)
            adapter.submitList(ordersFindMy)
        }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


    }

    private fun createDiscountView(
        percentage: String,
        description: String,
        context: Context
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_discount, null)

        val percentageTextView: TextView = view.findViewById(R.id.discount_percentage)
        val descriptionTextView: TextView = view.findViewById(R.id.discount_description)

        percentageTextView.text = percentage
        descriptionTextView.text = description

        return view
    }

    private fun realizeChipGroup(characters: List<UserDiscount>) {

        binding.chipGroup.removeAllViews()
        for (char in characters) {
            val discountView = createDiscountView(
                "${char.value}%", char.userCategory?.title ?: "", binding.chipGroup.context
            )

            discountView.setOnClickListener {
                val bundle =
                    Bundle().apply { putString("category", char.userCategory?.slug ?: "index") }

                (activity as MainActivity).replacerFragment(
                    CategoryFragment().apply { arguments = bundle }
                )
            }
            binding.chipGroup.addView(discountView)
        }
    }


}