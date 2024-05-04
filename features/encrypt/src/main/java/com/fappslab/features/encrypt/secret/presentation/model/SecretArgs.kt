package com.fappslab.features.encrypt.secret.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SecretArgs(
    val alias: String,
    val passphrase: String
) : Parcelable
