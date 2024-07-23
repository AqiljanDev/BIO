package com.example.bio.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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

fun <T> MutableList<T>.toggleItemBasedOnList(item: T, checkList: List<T>) {
    if (!checkList.contains(item)) {
        if (!contains(item)) {
            add(item)
        }else
            remove(item)
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

fun Context.vibratePhone(duration: Long = 50) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        // Deprecated in API 26
        vibrator.vibrate(duration)
    }
}
