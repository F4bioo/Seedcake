package com.fappslab.features.common.navigation

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface DetailsNavigation {
    fun createDetailsIntent(context: Context, args: DetailsArgs): Intent
}

@Parcelize
data class DetailsArgs(
    val id: Int,
    val alias: String,
    val date: String,
    val unreadableSeedPhrase: String
) : Parcelable
