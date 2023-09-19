package com.fappslab.seedcake.libraries.extension.args

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal const val KEY_ARGS = "key_args"

class ArgsProperty<H, P : Parcelable>(
    private val clazz: Class<P>,
    private val bundle: H.() -> Bundle
) : ReadOnlyProperty<H, P> {

    private var parcelable: P? = null

    override fun getValue(thisRef: H, property: KProperty<*>): P {
        val args = parcelable ?: bundle(thisRef).toParcelable(clazz)
        return requireNotNull(args) {
            "Cannot read property ${property.name} have you invoked putArguments?"
        }
    }
}

private fun <P : Parcelable> Bundle.toParcelable(clazz: Class<P>): P? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        @Suppress("DEPRECATION") getParcelable(KEY_ARGS)
    } else getParcelable(KEY_ARGS, clazz)
}

fun <P : Parcelable> Intent.getParcelableCompat(extra: String, clazz: Class<P>): P? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        @Suppress("DEPRECATION") getParcelableExtra(extra)
    } else getParcelableExtra(extra, clazz)
}
