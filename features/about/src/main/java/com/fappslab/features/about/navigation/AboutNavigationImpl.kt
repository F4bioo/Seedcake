package com.fappslab.features.about.navigation

import androidx.fragment.app.DialogFragment
import com.fappslab.features.about.presentation.AboutDialog
import com.fappslab.features.common.navigation.AboutNavigation

internal class AboutNavigationImpl : AboutNavigation {

    override fun createAboutDialog(): DialogFragment =
        AboutDialog.createFragment()
}
