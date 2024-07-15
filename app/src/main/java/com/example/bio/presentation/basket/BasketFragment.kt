package com.example.bio.presentation.basket

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bio.R
import com.example.bio.data.dto.CreateCheckout
import com.example.bio.data.dto.PostCartDto
import com.example.bio.data.dto.ProductBasketCreate
import com.example.bio.databinding.FragmentBasketBinding
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.BasketAdapter
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.card.ProductCardFragment
import com.example.data.SharedPreferencesManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class BasketFragment : BaseBottomFragment() {

    private val viewModel: BasketViewModel by viewModels()
    private val binding: FragmentBasketBinding by lazy {
        FragmentBasketBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private lateinit var adapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BasketAdapter(
            products = listOf(),
            listOf(),
            clickToEvent = { prodId, count -> eventCart(prodId, count) },
            clickDeleteBasket = { id, totalPrice -> deleteCart(id, totalPrice) },
            clickToCard = { product -> clickToCard(product) },
            updateTotalPrice = { totalPrice -> updateTotalPrice(totalPrice) } // Pass the callback
        )

        binding.rcBaskets.adapter = adapter

        viewModel.getCartFull(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))

        combine(
            viewModel.cartFull,
            viewModel.profileDiscount
        ) { cartFull, userDiscounts ->
            Pair(cartFull, userDiscounts)
        }.onEach { (cartFull, userDiscounts) ->
            if (cartFull.totalCount == 0) {
                binding.llListTrue.visibility = View.GONE
                binding.tvBasketIsClear.visibility = View.VISIBLE
            } else {
                binding.llListTrue.visibility = View.VISIBLE
                binding.tvBasketIsClear.visibility = View.GONE
            }

            adapter.updateLists(cartFull.products, userDiscounts)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        val dividerItemDecoration =
            DividerItemDecoration(binding.rcBaskets.context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider)
        if (drawable != null) {
            dividerItemDecoration.setDrawable(drawable)
        }
        binding.rcBaskets.addItemDecoration(dividerItemDecoration)

        val spinnerAdapterMethodDelivery = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.method_delivery)
        )
        binding.spinnerMethodDelivery.adapter = spinnerAdapterMethodDelivery

        binding.spinnerMethodDelivery.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            binding.tvAddress.visibility = View.GONE
                            binding.editTextAddress.visibility = View.GONE
                        }

                        1 -> {
                            binding.tvAddress.visibility = View.VISIBLE
                            binding.editTextAddress.visibility = View.VISIBLE
                        }

                        else -> {
                            Log.d("Mylog", "Position not yet implemented")
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No implementation needed
                }
            }

        val spinnerAdapterCheckAccount = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.checking_account)
        )
        binding.spinnerCheckingAccount.adapter = spinnerAdapterCheckAccount

        binding.btnChecout.setOnClickListener {
            val methodDeliveryId = binding.spinnerMethodDelivery.selectedItemId + 1
            val checkAccountId = binding.spinnerCheckingAccount.selectedItemId + 1
            val address = binding.tvAddress.text
            val comment = binding.tvComment.text
            var listProduct = listOf<ProductBasketCreate>()

            viewModel.getCartFull(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                combine(
                    viewModel.cartFull,
                    viewModel.profileDiscount,
                    viewModel.billMy
                ) { cart, profile, bill ->
                    Triple(cart, profile, bill)
                }.collect { (cart, profile, bill) ->
                    listProduct = cart.products.map {
                        ProductBasketCreate(
                            it.id,
                            it.count,
                            it.product.price,
                            it.product.discountPrice(profile).discountValue.toString()
                        )
                    }
                    val productsGson = Gson().toJson(listProduct)

                    Log.d("Mylog", "Product === $productsGson")

                    val product = CreateCheckout(
                        address.toString(),
                        comment.toString(),
                        if (bill.isEmpty()) 1 else bill[0].usersId,
                        methodDeliveryId.toInt(),
                        "Скидка 20",
                        productsGson
                    )

                    // Здесь мы вызываем createOrder только один раз, когда product заполнен
                    viewModel.createOrder(
                        sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
                        product
                    )
                    viewModel.getCartFull(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))

                    // Прерываем сбор после одного вызова
                    cancel()
                }
            }
        }


    }

    fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun eventCart(prodId: String, count: Int) {
        viewModel.postCart(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            PostCartDto(prodId, count)
        )
    }

    private fun deleteCart(id: Int, totalPrice: Int) {
        viewModel.deleteCart(
            sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN),
            id
        )
        updateTotalPrice(totalPrice)
        viewModel.getCartFull(sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN))
    }

    private fun clickToCard(product: Product) {
        val bundle = Bundle().apply { putString("category", product.slug) }
        (activity as MainActivity).replacerFragment(
            ProductCardFragment().apply {
                arguments = bundle
            }
        )
    }

    private fun updateTotalPrice(totalPrice: Int) {
        Log.d("Mylog", "Price $totalPrice")
        val formattedPrice = NumberFormat.getNumberInstance(Locale("ru", "RU")).format(totalPrice)
        binding.tvPriceRes.text = "Итого:  $formattedPrice ₸"
    }
}
