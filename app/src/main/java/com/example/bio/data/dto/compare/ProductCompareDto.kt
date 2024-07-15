package com.example.bio.data.dto.compare

import android.util.Log
import com.example.bio.data.dto.DiscountDto
import com.example.bio.domain.entities.compare.ProductCompare
import com.example.bio.domain.entities.userDiscount.UserCategory
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.presentation.data.PriceDiscount

data class ProductCompareDto (
    override val id1c: String,
    override val title: String,
    override val slug: String,
    override val photo: String?,
    override val count: Int,
    override val price: Int,
    override val discount: DiscountDto?,
    override val categories: CategoryCompareDto,
    override val characters: List<String>,
    override val categoriesId: String
): ProductCompare {

    override fun discountPrice(listProfileDiscount: List<UserDiscount>): PriceDiscount {
        val product = this

        Log.d("discount", "Product:: Discount = ${product.discount}")

        // PRODUCT
        if (product.discount != null) {

            val discount = product.discount

            val price = if (discount.type == 1) {
                product.price - ((product.price * discount.value) / 100)
            } else product.price - discount.value
            Log.d(
                "discount",
                "Product:: Discount type = ${discount.type}, value = ${discount.value}"
            )

            return PriceDiscount(
                price,
                discount.type,
                discount.value
            )
        }

        // CATEGORY AND PARENT CATEGORY
        var discountCategory = product.categories.discount
        Log.d("discount", "Category:: Discount = $discountCategory")

        if (discountCategory == null) {
            val discountParentCat = product.categories.parentCategory

            fun findDiscountInCategory(category: ParentCategoryCompareDto?) {
                var currentCategory = category
                while (currentCategory != null) {
                    if (currentCategory.discount != null) {
                        discountCategory = currentCategory.discount
                        return
                    }
                    currentCategory = currentCategory.parentCategory
                }
            }
            if (discountParentCat != null) findDiscountInCategory(discountParentCat)

        }

        discountCategory?.let {
            Log.d(
                "discount",
                "Category:: Discount type = ${discountCategory!!.type}, value = ${discountCategory!!.value}"
            )

            val price = if (it.type == 1) {
                product.price - ((product.price * it.value) / 100)
            } else product.price - it.value

            return PriceDiscount(
                price,
                it.type,
                it.value
            )
        }


        Log.d("discount", "User:: Discount list = ${listProfileDiscount.size}")
        // USER MANY DISCOUNTS
        if (listProfileDiscount.isNotEmpty()) {

            var discount: UserDiscount? = null

            listProfileDiscount.forEach {
                val discountCats = it.userCategory

                val catIds = mutableListOf<String>()

                fun discountCatsFunc(cat: UserCategory) {
                    catIds.add(cat.id1c)

                    if (cat.childCategory != null) {

                        cat.childCategory!!.forEach {
                            if (!catIds.contains(it.id1c)) {
                                catIds.add(it.id1c)
                            }

                            if (it.childCategory != null) {
                                discountCatsFunc(it)
                            }
                        }
                    }
                }

                if (discountCats != null) discountCatsFunc(discountCats)

                if (catIds.contains(product.categoriesId)) {
                    discount = it
                }
            }
            Log.d("discount", "User:: Discount = $discount")
            discount?.let {
                Log.d(
                    "discount",
                    "User:: Discount type = ${discount!!.type}, value = ${discount!!.value}"
                )

                val price = if (it.type == 1) {
                    product.price - ((product.price * it.value) / 100)
                } else product.price - it.value

                return PriceDiscount(
                    price,
                    it.type,
                    it.value
                )
            }
        }


        // NOT DISCOUNTS
        return PriceDiscount(
            price = product.price,
            discountType = 0,
            discountValue = 0
        )
    }
}
