package com.fappslab.features.details.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.seedcake.features.details.R
import com.fappslab.seedcake.features.details.databinding.DetailsActivityBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.args.putArgs
import com.fappslab.seedcake.libraries.extension.args.provideIntent
import com.fappslab.seedcake.libraries.extension.args.viewArgs

class DetailsActivity : AppCompatActivity(R.layout.details_activity) {

    private val binding: DetailsActivityBinding by viewBinding(R.id.details_root)
    private val args: DetailsArgs by viewArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        supportFragmentManager.commit {
            replace(binding.fragmentContainer.id, DetailsFragment.createFragment(args))
        }
    }

    companion object {
        fun createIntent(context: Context, args: DetailsArgs): Intent =
            context.provideIntent<DetailsActivity> { putArgs(args) }
    }
}
