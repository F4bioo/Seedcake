package com.fappslab.seedcake.libraries.extension

import android.content.res.Resources

private const val ROUNDING = 0.5f

fun Int?.orZero() = this ?: 0

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + ROUNDING).toInt()
