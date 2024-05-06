package com.fappslab.seedcake.libraries.extension

inline fun <reified T> Any.safeCast(block: T.() -> Unit) {
    (this as? T)?.also { block() }
}
