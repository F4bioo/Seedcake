package com.fappslab.seedcake.libraries.arch.source

interface Source<T> {
    fun create(): T
}
