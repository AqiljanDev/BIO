package com.example.bio.presentation.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.bio.R


class PaginationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var btnPrevious: ImageButton
    private var pageNumbersContainer: LinearLayout
    private var btnNext: ImageButton
    private var etGoToPage: EditText
    private var btnGoToPage: Button

    private var currentPage = 1

    private var totalPages = 1

    var onPageSelected: ((Int) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.pagination_view, this, true)

        btnPrevious = findViewById(R.id.btnPrevious)
        pageNumbersContainer = findViewById(R.id.pageNumbersContainer)
        btnNext = findViewById(R.id.btnNext)
        etGoToPage = findViewById(R.id.etGoToPage)
        btnGoToPage = findViewById(R.id.btnGoToPage)

        setupListeners()
        updateView()
    }

    private fun setupListeners() {
        btnPrevious.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updateView()
                onPageSelected?.invoke(currentPage)
            }
        }

        btnNext.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage++
                updateView()
                onPageSelected?.invoke(currentPage)
            }
        }

        btnGoToPage.setOnClickListener {
            val page = etGoToPage.text.toString().toIntOrNull()
            if (page != null && page in 1..totalPages) {
                currentPage = page
                updateView()
                onPageSelected?.invoke(currentPage)
            }
        }
    }

    private fun updateView() {
        pageNumbersContainer.removeAllViews()
        val buttonSize = resources.getDimensionPixelSize(R.dimen.twenty) // Размер кнопки

        val layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
        layoutParams.setMargins(4, 0, 4, 0) // Установка отступов между кнопками

        for (i in 1..totalPages) {
            if (i == 1 || i == totalPages || (i >= currentPage - 1 && i <= currentPage + 1)) {
                val textView = TextView(context).apply {
                    text = i.toString()
                    textSize = 11f
                    this.layoutParams = layoutParams
                    gravity = Gravity.CENTER
                    setBackgroundResource(R.drawable.button_page_background_passive)
                    setPadding(8, 8, 8, 8)
                    if (i == currentPage) {
                        setBackgroundResource(R.drawable.button_page_background_active)
                    } else {
                        setOnClickListener {
                            currentPage = i
                            updateView()
                            onPageSelected?.invoke(currentPage)
                        }
                    }
                }
                pageNumbersContainer.addView(textView)
            } else if (i == currentPage - 2 || i == currentPage + 2) {
                val textView = TextView(context).apply {
                    text = "..."
                    this.layoutParams = layoutParams
                    gravity = Gravity.CENTER
                    setPadding(5, 8, 5, 8)
                }
                pageNumbersContainer.addView(textView)
            }
        }
    }

    fun updateTotalPages(newTotalPages: Int) {
        this.totalPages = newTotalPages
        updateView()
    }

    fun resetCurrentPage() {
        currentPage = 1
    }
}
