package com.fappslab.seedcake.application

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

class ApplicationLifecycle(
    private val foregroundedBlock: () -> Unit
) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) foregroundedBlock()
    }

    fun registerApplicationLifecycleCallbacks() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun unregisterApplicationLifecycleCallbacks() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}
