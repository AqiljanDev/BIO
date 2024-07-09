package com.example.bio.utils

import android.graphics.Color
import com.google.android.material.badge.BadgeDrawable

fun BadgeDrawable.defaultCustom(
    horizontalSize: Int,
    textColor: Int = Color.RED,
    gravity: Int = BadgeDrawable.TOP_END,
    visible: Boolean = false,
    backgroundColor: Int = Color.RED
) {
    horizontalOffset = horizontalSize
    badgeTextColor = textColor
    badgeGravity = gravity
    isVisible = visible
    this.backgroundColor = backgroundColor
}

fun <T> MutableList<T>.toggleItem(item: T) {
    if (contains(item)) {
        remove(item)
    } else {
        add(item)
    }
}

fun <T> MutableList<T>.toggleItems(items: List<T>) {
    items.forEach { item ->
        if (contains(item)) {
            remove(item)
        } else {
            add(item)
        }
    }
}

