package com.example.bio.extension

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