package com.fappslab.seedcake.libraries.extension.args

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty

inline fun <reified P : Parcelable> Fragment.viewArgs(): ReadOnlyProperty<Fragment, P> {
    return ArgsProperty(P::class.java) {
        arguments ?: error("Did you invoke putArgs?")
    }
}

fun Bundle.putArgs(args: Parcelable) {
    putParcelable(KEY_ARGS, args)
}

fun <F : Fragment> F.withArgs(args: Bundle.() -> Unit): F {
    return apply { arguments = Bundle().apply(args) }
}
