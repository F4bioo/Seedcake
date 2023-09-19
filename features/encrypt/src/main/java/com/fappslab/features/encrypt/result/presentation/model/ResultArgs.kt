package com.fappslab.features.encrypt.result.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultArgs(
    val alias: String,
    val encryptedSeed: String
) : Parcelable
