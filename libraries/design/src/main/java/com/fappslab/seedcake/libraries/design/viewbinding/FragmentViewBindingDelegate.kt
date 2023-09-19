package com.fappslab.seedcake.libraries.design.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<Binding : ViewBinding>(
    private val bindingClass: Class<Binding>
) : ReadOnlyProperty<Fragment, Binding>, DefaultLifecycleObserver {

    private var binding: Binding? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): Binding =
        thisRef.binding()

    override fun onDestroy(owner: LifecycleOwner) {
        binding = null
        owner.lifecycle.removeObserver(this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun Fragment.binding(): Binding {
        binding?.let { return it }

        viewLifecycleOwner.lifecycle.addObserver(this@FragmentViewBindingDelegate)

        val inflateMethod = bindingClass.getMethod("bind", View::class.java)

        val invokeLayout = inflateMethod.invoke(null, requireView()) as Binding

        return invokeLayout.also { binding = it }
    }
}

inline fun <reified Binding : ViewBinding> Fragment.viewBinding() =
    FragmentViewBindingDelegate(Binding::class.java)
