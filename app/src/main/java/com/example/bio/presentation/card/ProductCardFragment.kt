package com.example.bio.presentation.card

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bio.R
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.databinding.FragmentProductCardBinding
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOne.Categories
import com.example.bio.domain.entities.findOne.GalleryItem
import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.presentation.MainActivity
import com.example.bio.presentation.adapter.CategoryAdapter
import com.example.bio.presentation.adapter.CharactersCardAdapter
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.data.ProductCardQuad
import com.example.bio.utils.vibratePhone
import com.example.core.UrlConstants
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ProductCardFragment : Fragment() {
    private var request: String? = ""

    private val viewmodel: ProductCardViewModel by viewModels()
    private val binding: FragmentProductCardBinding by lazy {
        FragmentProductCardBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: CategoryAdapter
    private val sharedPreferences by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token: String by lazy { sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN) }

    private var listFavorite: MutableList<String> = mutableListOf()
    private var listGroup: MutableList<String> = mutableListOf()

    private var isFavoriteActive: Boolean = false
    private var isGroupActive: Boolean = false
    private var countProducts: Int = 0
    private var idCard: Int = 0

    private lateinit var dots: Array<ImageView?>
    private var currentImageIndex = 0
    private var downX: Float = 0f

    private val bottomNavigationView: BottomNavigationView? by lazy {
        activity?.findViewById(R.id.bottom_navigation)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            request = it.getString("category") ?: "index"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        request?.let {
            viewmodel.getProductCard(
                token, it
            )
        }

        setupAdapter()
        observeViewModel()

        viewmodel.product.onEach {
            initializeFragment(it)
            ifElementIsEmpty(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun setupAdapter() {
        adapter = CategoryAdapter(
            listOf(),
            listOf(),
            listOf(),
            CartMiniDto(emptyList(), 0),
            listOf(),
            { isState, id1c -> updateFavorite(isState, id1c) },
            { isState, id1c -> updateGroup(isState, id1c) },
            { prodId, count -> updateBasket(prodId, count) },
            { id -> deleteBasket(id) }
        ) { product ->
            val bundle = Bundle().apply { putString("category", product.slug) }
            (activity as MainActivity).apply {
                replacerFragment(ProductCardFragment().apply { arguments = bundle })
            }
        }
        binding.rcProductsOther.adapter = adapter
    }

    private fun observeViewModel() {
        combine(
            viewmodel.product,
            viewmodel.wishListMini,
            viewmodel.compareMini,
            viewmodel.cartMini,
            viewmodel.profileDiscount
        ) { product, wishList, compareList, cart, profile ->
            ProductCardQuad(product, wishList, compareList, cart, profile)
        }.onEach { (product, wishList, compareList, cart, profile) ->
            adapter.updateLists(product.other, wishList, compareList, cart, profile)

            updateConditionProduct(product, wishList, compareList, cart)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateConditionProduct(
        product: FindOneProduct,
        wishList: List<WishListCompareMini>,
        compareList: List<WishListCompareMini>,
        cart: CartMini
    ) {
        isFavoriteActive = false
        wishList.forEach {
            if (it.prodId == product.id1c) isFavoriteActive = true
        }
        binding.imgBtnLike.setImageResource(if (isFavoriteActive) R.drawable.ic_heath_active else R.drawable.heart_passive)

        isGroupActive = false
        compareList.forEach {
            if (it.prodId == product.id1c) isGroupActive = true
        }
        binding.imgBtnGroup.setImageResource(if (isGroupActive) R.drawable.ic_group_active else R.drawable.ic_group)

        cart.products.forEach {
            if (it.prodId == product.id1c) {
                countProducts = it.count
                idCard = it.id
                return@forEach
            }
        }
        binding.tvCountMy.text = countProducts.toString()
        if (countProducts > 0) {
            binding.consLayoutBasketActive.visibility = View.VISIBLE
            binding.btnBasket.visibility = View.GONE
        } else {
            binding.consLayoutBasketActive.visibility = View.GONE
            binding.btnBasket.visibility = View.VISIBLE
        }
    }

    private fun updateBasket(prodId: String, count: Int) {
        viewmodel.eventCart(token, PostCartDto(prodId, count))
        (activity as MainActivity).badgeBasket.isVisible = true

        requireContext().vibratePhone()
    }

    private fun deleteBasket(id: Int) {
        try {
            Log.d("Mylog", "ID count = $id")
            viewmodel.deleteCart(token, id)
        } catch (ex: Exception) {
            Log.d("Mylog", "Exception === ${ex.message}")
        }
        (activity as MainActivity).badgeBasket.isVisible = false
    }

    private fun updateGroup(state: Boolean, id1c: String) {
        if (state) listGroup.add(id1c) else listGroup.remove(id1c)

        (activity as MainActivity).badgeGroup.isVisible = listGroup.isNotEmpty()
        if (listGroup.isNotEmpty()) requireContext().vibratePhone()
        viewmodel.eventCompare(token, id1c)
    }

    private fun updateFavorite(state: Boolean, id1c: String) {
        if (state) listFavorite.add(id1c) else listFavorite.remove(id1c)
        (activity as MainActivity).badgeFavorite.isVisible = listFavorite.isNotEmpty()

        if (listFavorite.isNotEmpty()) requireContext().vibratePhone()

        viewmodel.eventWishList(token, id1c)
    }

    private fun ifElementIsEmpty(product: FindOneProduct) {
        if (product.gallery.isEmpty()) binding.viewFlipper.setBackgroundResource(R.drawable.camera_slash)

        if (product.brands == null) binding.tvBrand.visibility = View.GONE

        if (product.desc.isEmpty()) with(binding) {
            viewLine.visibility = View.GONE
            tvDesc.visibility = View.GONE
            btnDescription.visibility = View.GONE
            llDescIew.visibility = View.GONE
        }

        if (product.charactersToProducts.isEmpty()) with(binding) {
            btnCharacteristics.visibility = View.GONE
            llCharactersView.visibility = View.GONE
            btnDescription.isSelected = true
        }

        if (product.other.isEmpty()) with(binding) {
            rcProductsOther.visibility = View.GONE
            llSimilarProducts.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initializeFragment(product: FindOneProduct) = with(binding) {
        val formatMoney = NumberFormat.getNumberInstance(Locale("ru", "RU"))
        Log.d("Mylog", "Product card = product image: ${product.gallery.size}")

        initializeChips(product.categories)

        viewmodel.profileDiscount.onEach {
            val discount = product.discountPrice(it)

            tvPriceProcent.visibility = if (discount.discountType == 0) View.GONE else View.VISIBLE

            tvCurrentPrice.text = "${formatMoney.format(discount.price)} ₸"
            tvPriceProcent.text = "${formatMoney.format(product.price)} ₸"
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnPlus.setOnClickListener {
            if (countProducts < product.count) {
                countProducts++
                updateBasket(product.id1c, countProducts)
                binding.tvCountMy.text = countProducts.toString()
            }
        }

        binding.btnMinus.setOnClickListener {
            if (countProducts > 1) {
                countProducts--
                updateBasket(product.id1c, countProducts)
                binding.tvCountMy.text = countProducts.toString()
            } else {
                countProducts--
                binding.consLayoutBasketActive.visibility = View.GONE
                binding.btnBasket.visibility = View.VISIBLE
                // Для удаления из корзины при количестве 1 можно использовать следующую строку
                deleteBasket(idCard)
            }
        }

        binding.btnBasket.setOnClickListener {
            countProducts = 1
            updateBasket(product.id1c, countProducts)
            binding.consLayoutBasketActive.visibility = View.VISIBLE
            binding.btnBasket.visibility = View.GONE
            binding.tvCountMy.text = countProducts.toString()
        }

        tvCategoryCurrentFull.text = product.titleFull

        tvApt.text = "apt: ${product.article}"
        if (product.count > 0) {
            imageViewYesNo.setImageResource(R.drawable.ic_in_stock_yes)
            tvCount.text = "${formatMoney.format(product.count)} штук"
        }

        if (product.gallery.isNotEmpty()) {
            initializeSlider(product.gallery)
        }

        tvBrand.text = product.categories.title
        tvDesc.text = product.desc
        tvPriceProcent.paintFlags = tvPriceProcent.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        if (product.charactersToProducts.isEmpty()) {
            btnDescription.isSelected = true
            viewSwitcher.displayedChild = 1
        } else btnCharacteristics.isSelected = true

        btnCharacteristics.setOnClickListener {
            viewSwitcher.displayedChild = 0
            btnCharacteristics.isSelected = true
            btnDescription.isSelected = false
        }

        btnDescription.setOnClickListener {
            viewSwitcher.displayedChild = 1
            btnDescription.isSelected = true
            btnCharacteristics.isSelected = false
        }

        rcCharactersFull.adapter = CharactersCardAdapter(product.charactersToProducts)
        tvDescFull.text = product.desc

        imgBtnLike.setOnClickListener {
            updateFavorite(!isFavoriteActive, product.id1c)
        }

        imgBtnGroup.setOnClickListener {
            updateGroup(!isGroupActive, product.id1c)
        }

    }


    private fun initializeSlider(productImages: List<GalleryItem>) {
        dots = arrayOfNulls(productImages.size)
        Log.d("Mylog", "Initialize slider open")

        for (i in productImages.indices) {
            val imageView = ImageView(context)
            Log.d("Mylog", "Images = ${productImages[i].photo}")
            Glide.with(requireContext())
                .load( UrlConstants.IMG_PRODUCT_URL + productImages[i].photo)
                .into(imageView)

            imageView.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT,
            )
            binding.viewFlipper.addView(imageView)
            addDot(i)
        }

        updateDots(0)

        binding.viewFlipper.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                event?.let {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = it.x
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
                            val upX = it.x
                            if (upX < downX) {
                                onSwipeLeft()
                            } else if (upX > downX) {
                                onSwipeRight()
                            }
                            return true
                        }

                        else -> {}
                    }
                }
                return false
            }

            private fun onSwipeLeft() {
                binding.viewFlipper.setInAnimation(context, R.anim.slide_in_right)
                binding.viewFlipper.setOutAnimation(context, R.anim.slide_out_left)
                binding.viewFlipper.showNext()
                currentImageIndex = (currentImageIndex + 1) % productImages.size
                updateDots(currentImageIndex)
            }

            private fun onSwipeRight() {
                binding.viewFlipper.setInAnimation(context, R.anim.slide_in_left)
                binding.viewFlipper.setOutAnimation(context, R.anim.slide_out_right)
                binding.viewFlipper.showPrevious()
                currentImageIndex =
                    (currentImageIndex - 1 + productImages.size) % productImages.size
                updateDots(currentImageIndex)
            }
        })
    }

    private fun initializeChips(categories: Categories) {
        binding.cgHistoryCatalog.removeAllViews()
        var currentCategory = categories.parentCategory

        addChipHistoryCategory(categories.title, categories.slug)

        while (currentCategory != null) {
            addDividerText("/")
            addChipHistoryCategory(currentCategory.title, currentCategory.slug)
            currentCategory = currentCategory.parentCategory
        }
    }

    private fun addChipHistoryCategory(text: String, slug: String) {
        val chip = Chip(context).apply {
            this.text = text
            setChipBackgroundColorResource(R.color.chip_background_color)
            setTextColor(Color.BLACK)
            chipStartPadding = 0f
            chipEndPadding = 0f

            setEnsureMinTouchTargetSize(false)
            chipStrokeWidth = 0f
            isClickable = true
            isCheckable = false

            setOnClickListener {
                val bundle = Bundle().apply { putString("category", slug) }
                (activity as MainActivity).replacerFragment(CategoryFragment().apply {
                    arguments = bundle
                })
            }
        }
        binding.cgHistoryCatalog.addView(chip, 0)
    }

    private fun addDividerText(text: String) {
        val chip = Chip(context).apply {
            this.text = text
            textSize = 22f
            gravity = Gravity.CENTER
            setEnsureMinTouchTargetSize(false)

            chipStartPadding = 0f
            chipEndPadding = 0f
            chipStrokeWidth = 0f
        }
        binding.cgHistoryCatalog.addView(chip, 0)
    }

    private fun addDot(index: Int) {
        val dot = ImageView(context).apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.nonactive_dot
                )
            )
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
            }
        }
        dots[index] = dot
        binding.dotLayout.addView(dot)
    }

    private fun updateDots(currentIndex: Int) {
        for (i in dots.indices) {
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (i == currentIndex) R.drawable.active_dot else R.drawable.nonactive_dot
                )
            )
        }
    }
}




