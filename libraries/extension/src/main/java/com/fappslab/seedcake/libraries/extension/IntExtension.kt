package com.fappslab.seedcake.libraries.extension

import android.content.res.Resources

fun Int?.orZero() = this ?: 0

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
