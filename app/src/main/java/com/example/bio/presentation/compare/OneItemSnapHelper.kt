package com.example.bio.presentation.compare

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class OneItemSnapHelper : PagerSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        val snapView = super.findSnapView(layoutManager)
        if (snapView != null) {
            val halfWidth = snapView.width / 2
            // Устанавливаем ширину элемента в половину ширины экрана
            snapView.layoutParams.width = halfWidth * 2
            snapView.requestLayout()
        }
        return snapView
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val out = super.calculateDistanceToFinalSnap(layoutManager, targetView)
        out?.let {
            // Прокручиваем по одному элементу за раз
            it[0] /= 2
        }
        return out
    }
}


