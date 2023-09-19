package com.fappslab.seedcake.libraries.extension

import android.content.res.Resources

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density
