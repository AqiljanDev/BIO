package com.example.bio.presentation.profile.orders.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentOrderHistoryCardBinding
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.OrderHistoryCardAdapter
import com.example.bio.presentation.profile.orders.OrdersFragment
import com.example.bio.utils.vibratePhone
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class OrderHistoryCardFragment : Fragment() {
    private var currentId: Int? = null

    private val viewModel: OrderHistoryCardViewModel by viewModels()
    private val binding: FragmentOrderHistoryCardBinding by lazy {
        FragmentOrderHistoryCardBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token by lazy {
        sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)
    }

    private val bottomNavigationView: BottomNavigationView? by lazy {
        activity?.findViewById(R.id.bottom_navigation)
    }

    private val formatMoney = NumberFormat.getNumberInstance(Locale("ru", "RU"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            currentId = it.getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentId?.let {
            viewModel.getOrderFindOne(token, it)
        }

        val adapter = OrderHistoryCardAdapter(
            { prodId, count ->  updateBasket(prodId, count) },
            { id -> deleteBasket(id) }
        )
        binding.rcHistoryProducts.adapter = adapter

        Log.d("Mylog", "Id = $currentId")

        combine(
            viewModel.orderFindOne,
            viewModel.cartMini
        ) { order, cart ->
            Pair(order, cart)
        }.onEach { (order, cart) ->
            with(binding) {

                tvOrderNumber.text = "Заказ #${order.id}"
                tvOrderDate.text = order.date
                tvStatusOrder.text = order.orderStatus.name
                tvBillValue.text = order.userBill.bank
                tvAddressValue.text = order.address

                // 1 - Самовывоз 2- Доставка
                tvDeliveryValue.text =
                    getString(if (order.deliverId == 2) R.string.delivery_pickup else R.string.delivery_delivery)

                tvAdministratorValue.text =
                    if (order.admins != null) order.admins!!.email else getString(R.string.indefined)
                tvWorderSum.text =
                    "Сумма: ${formatMoney.format(order.products.sumOf { it.count * it.price })} ₸"

                btnRepeatOrder.setOnClickListener {

                    order.products.forEach {  orderProduct ->
                        if (!cart.products.any { it.prodId == orderProduct.id1c }) {
                            updateBasket(orderProduct.id1c, 1)

                            adapter.updateList(order.products, cart)
                        }
                    }
                }

                adapter.updateList(order.products, cart)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        (activity as MainActivity).onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        OrdersFragment()
                    ).commit()
            }

        })

    }

    private fun updateBasket(prodId: String, count: Int) {
        viewModel.eventCart(token, PostCartDto(prodId, count))
        (activity as MainActivity).badgeBasket.isVisible = true

        requireContext().vibratePhone()
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
}