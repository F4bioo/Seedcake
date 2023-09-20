package com.fappslab.features.data.local.db.client

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.fappslab.seedcake.libraries.arch.source.Source

internal class SecurePreferences(
    private val context: Context,
    private val name: String
) : Source<SharedPreferences> {

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            name,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun create(): SharedPreferences {
        return prefs
    }
}
