package com.fappslab.seedcake.libraries.extension.args

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty

inline fun <reified P : Parcelable> FragmentActivity.viewArgs(): ReadOnlyProperty<Activity, P> {
    return ArgsProperty(P::class.java) {
        intent.extras ?: error("Did you invoke putArgs?")
    }
}

inline fun <reified A : Activity> Context.toIntent(
    flags: Int? = null,
    params: Intent.() -> Unit = {}
): Intent {
    return Intent(this, A::class.java)
        .apply(params)
        .also { intent -> flags?.let { intent.flags = it } }
}

fun Intent.putArgs(args: Parcelable): Intent {
    return putExtra(KEY_ARGS, args)
}
