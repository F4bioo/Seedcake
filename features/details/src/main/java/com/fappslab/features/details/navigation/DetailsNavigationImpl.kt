package com.fappslab.features.details.navigation

import android.content.Context
import android.content.Intent
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.features.common.navigation.DetailsNavigation
import com.fappslab.features.details.presentation.DetailsActivity

internal class DetailsNavigationImpl : DetailsNavigation {

    override fun createDetailsIntent(context: Context, args: DetailsArgs): Intent =
        DetailsActivity.createIntent(context, args)
}
