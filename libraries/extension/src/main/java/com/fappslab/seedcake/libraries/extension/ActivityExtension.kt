package com.fappslab.seedcake.libraries.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.activityLauncher(
    resultBLock: (Intent?) -> Unit
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) resultBLock(result.data)
    }
}

fun FragmentActivity.onFragmentResult(requestKey: String, bundleBlock: Bundle.() -> Unit) {
    supportFragmentManager.setFragmentResultListener(
        requestKey,
        this
    ) { _, bundle ->
        bundleBlock(bundle)
    }
}

fun FragmentActivity.addBackPressedCallback(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val backCallback = object : OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }
    onBackPressedDispatcher.addCallback(this, backCallback)
}
