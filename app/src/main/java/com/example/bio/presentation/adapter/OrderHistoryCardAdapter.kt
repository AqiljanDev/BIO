package com.example.bio.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bio.R
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.findOneOrder.FindOneOrderProductSDto
import com.example.bio.databinding.HistoryCardProductBinding
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOneOrder.FindOneOrderProduct

class OrderHistoryCardAdapter(
    private val clickEventBasket: (prodId: String, count: Int) -> Unit,
    private val clickDeleteBasket: (id: Int) -> Unit,
) : ListAdapter<FindOneOrderProduct, OrderHistoryCardAdapter.HistoryCardProductViewHolder>(
    DiffCallBack()
) {
    private var cart: CartMini = CartMiniDto(listOf(), 0)

    inner class HistoryCardProductViewHolder(
        private val binding: HistoryCardProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(order: FindOneOrderProduct) = with(binding) {
            tvProductTitle.text = order.title
            tvProductArticle.text = "Арт.: ${order.article} - "
            tvProductDiscount.text =
                "Скидка: ${if (order.discount == "") "Нет" else order.discount}"
            tvProductPrice.text = "${order.price} ₸ - "
            tvProductCount.text = "${order.count}шт"

            if (checkBasket(order.id1c)) {
                btnProductButton.setBackgroundResource(R.drawable.button_background_black)
                btnProductButton.text = "Убрать"
            } else {
                btnProductButton.setBackgroundResource(R.drawable.button_background)
                btnProductButton.text = "Добавить"
            }

            // Проверка данных пикселей и установка изображения
            if (order.preview?.data.isNullOrEmpty()) {
                imageViewProduct.setImageResource(R.drawable.camera_slash)  // Установите изображение-заполнитель
            } else {
                try {
                    val bitmap = listToBitmap(order.preview!!.data, 70, 90)
                    imageViewProduct.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Log.d("Mylog", "${e.printStackTrace()}=== ${e.message}")
                    imageViewProduct.setImageResource(R.drawable.ic_launcher_foreground)  // Установите изображение-заполнитель для ошибок
                }
            }

            btnProductButton.setOnClickListener {
                if (!checkBasket(order.id1c)) {
                    clickEventBasket(order.id1c, 1)

                    btnProductButton.setBackgroundResource(R.drawable.button_background_black)
                    btnProductButton.text = "Убрать"
                } else {
                    cart.products.forEach {
                        if (order.id1c == it.prodId) {

                            clickDeleteBasket(it.id)
                            return@forEach
                        }
                    }

                    btnProductButton.setBackgroundResource(R.drawable.button_background)
                    btnProductButton.text = "Добавить"
                }
            }
        }
    }

    private fun checkBasket(id1c: String): Boolean {
        return cart.products.any { it.prodId == id1c }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryCardProductViewHolder {
        val view = HistoryCardProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryCardProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryCardProductViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    fun listToBitmap(pixelList: List<Int>, width: Int, height: Int): Bitmap {
        // Убедитесь, что количество пикселей соответствует ширине и высоте
        if (pixelList.size != width * height) {
            throw IllegalArgumentException("Pixel list size does not match the specified width and height")
        }

        // Создайте Bitmap с заданной шириной и высотой
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Установите пиксели в Bitmap
        bitmap.setPixels(pixelList.toIntArray(), 0, width, 0, 0, width, height)

        return bitmap
    }


    fun updateList(list: List<FindOneOrderProduct>, cartMini: CartMini) {
        cart = cartMini
        submitList(list)
    }

    class DiffCallBack : DiffUtil.ItemCallback<FindOneOrderProduct>() {
        override fun areItemsTheSame(
            oldItem: FindOneOrderProduct,
            newItem: FindOneOrderProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FindOneOrderProduct,
            newItem: FindOneOrderProduct
        ): Boolean {
            return oldItem as FindOneOrderProductSDto == newItem as FindOneOrderProductSDto
        }

    }
}