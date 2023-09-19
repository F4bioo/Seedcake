package com.fappslab.seedcake.libraries.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

fun Fragment.activityLauncher(
    resultBLock: (Intent?) -> Unit
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) resultBLock(result.data)
    }
}

fun Fragment.onFragmentResult(requestKey: String, bundleBlock: Bundle.() -> Unit) {
    activity?.supportFragmentManager?.setFragmentResultListener(
        requestKey,
        viewLifecycleOwner
    ) { _, bundle ->
        bundleBlock(bundle)
    }
}

fun <T> Fragment.setFragmentResult(requestKey: String, pair: Pair<String, T?>) {
    activity?.supportFragmentManager?.setFragmentResult(requestKey, bundleOf(pair))
}

fun Fragment.addBackPressedCallback(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val backCallback = object : OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backCallback)
}
