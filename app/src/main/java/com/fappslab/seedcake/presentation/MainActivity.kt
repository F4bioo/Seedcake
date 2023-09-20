package com.fappslab.seedcake.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import com.fappslab.features.common.navigation.AboutNavigation
import com.fappslab.features.common.navigation.LockNavigation
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.seedcake.R
import com.fappslab.seedcake.databinding.ActivityMainBinding
import com.fappslab.seedcake.di.AppModuleLoad
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.presentation.extension.addBackPressedCallback
import com.fappslab.seedcake.presentation.extension.navController
import com.fappslab.seedcake.presentation.viewmodel.MainViewAction
import com.fappslab.seedcake.presentation.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(R.layout.activity_main), KoinLazy {

    private val binding: ActivityMainBinding by viewBinding(R.id.main_root)
    private val viewModel: MainViewModel by viewModel()
    private val lockNavigation: LockNavigation by inject()
    private val aboutNavigation: AboutNavigation by inject()
    private val backCallback = addBackPressedCallback { isHomeFragment ->
        viewModel.onBackPressed(isHomeFragment)
    }

    override val scope: Scope by activityScope()

    override val koinLoad: KoinLoad by subModules(AppModuleLoad)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentContainer()
        setupObservables()
        setupBackPressed()
        setupToolbar()
    }

    private fun setupObservables() {
        onViewAction(viewModel) { action ->
            when (action) {
                MainViewAction.FinishView -> finish()
                MainViewAction.Protected -> protectedAction()
                MainViewAction.BackPressed -> backPressedAction()
                MainViewAction.About -> showAboutDialogAction()
            }
        }
    }

    private fun setupFragmentContainer() = binding.run {
        val navController = supportFragmentManager.navController(R.id.fragment_container_main)
        bottomNavigation.setupWithNavController(navController)
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, backCallback)
    }

    private fun setupToolbar() {
        binding.toolbarMain.run {
            setOnMenuItemClickListener {
                viewModel.onAboutItem()
                true
            }
        }
    }

    private fun protectedAction() {
        lockNavigation.createLockIntent(context = this, ScreenTypeArgs.LockScreen)
            .also(::startActivity)
    }

    private fun backPressedAction() {
        backCallback.isEnabled = false
        onBackPressedDispatcher.onBackPressed()
        backCallback.isEnabled = true
    }

    private fun showAboutDialogAction() {
        aboutNavigation.createAboutDialog()
            .show(supportFragmentManager, this::class.java.simpleName)
    }
}
