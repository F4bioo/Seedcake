package com.fappslab.features.common.navigation

import android.content.Context
import android.content.Intent

interface EncryptNavigation {
    fun createEncryptIntent(context: Context): Intent
}
