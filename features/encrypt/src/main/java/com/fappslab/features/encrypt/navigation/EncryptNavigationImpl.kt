package com.fappslab.features.encrypt.navigation

import android.content.Context
import android.content.Intent
import com.fappslab.features.common.navigation.EncryptNavigation
import com.fappslab.features.encrypt.main.presentation.EncryptActivity

internal class EncryptNavigationImpl : EncryptNavigation {

    override fun createEncryptIntent(context: Context): Intent =
        EncryptActivity.createIntent(context)
}
