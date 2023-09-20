package com.fappslab.seedcake.presentation.extension

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fappslab.features.home.presentation.HomeFragment
import com.fappslab.seedcake.R
import com.fappslab.seedcake.presentation.MainActivity

fun FragmentManager.navController(@IdRes containerId: Int): NavController {
    return (findFragmentById(containerId) as NavHostFragment).navController
}

fun MainActivity.addBackPressedCallback(onBackPressed: (Boolean) -> Unit): OnBackPressedCallback {
    return object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_main) as? NavHostFragment
            val isHomeFragment = navHostFragment?.childFragmentManager
                ?.findFragmentById(R.id.fragment_container_main) is HomeFragment

            onBackPressed(isHomeFragment)
        }
    }
}
