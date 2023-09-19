package com.fappslab.seedcake.libraries.design.viewbinding

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<Binding : ViewBinding>(
    private val bindingClass: Class<Binding>,
    @IdRes private val rootViewId: Int
) : ReadOnlyProperty<AppCompatActivity, Binding>, DefaultLifecycleObserver {

    private var binding: Binding? = null

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): Binding {
        return binding ?: createBinding(thisRef).also { binding = it }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        binding = null
        owner.lifecycle.removeObserver(this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(activity: AppCompatActivity): Binding {
        activity.lifecycle.addObserver(this)

        val inflateMethod = bindingClass.getMethod("bind", View::class.java)
        val rootView = activity.findViewById<View>(rootViewId)

        return inflateMethod.invoke(null, rootView) as Binding
    }
}

inline fun <reified Binding : ViewBinding> AppCompatActivity.viewBinding(@IdRes rootViewId: Int) =
    ActivityViewBindingDelegate(Binding::class.java, rootViewId)
