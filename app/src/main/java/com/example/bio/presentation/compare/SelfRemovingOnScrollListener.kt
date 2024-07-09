package com.example.bio.presentation.compare

import androidx.recyclerview.widget.RecyclerView


open class SelfRemovingOnScrollListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this)
        }
    }
}