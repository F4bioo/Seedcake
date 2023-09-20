package com.fappslab.features.data.local.db.client

import android.content.Context
import android.content.SharedPreferences
import com.fappslab.seedcake.libraries.arch.source.Source

internal class SimplePreferences(
    private val context: Context,
    private val name: String
) : Source<SharedPreferences> {

    private val prefs by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override fun create(): SharedPreferences {
        return prefs
    }
}
