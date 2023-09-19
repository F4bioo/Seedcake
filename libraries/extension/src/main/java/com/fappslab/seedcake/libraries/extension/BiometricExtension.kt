package com.fappslab.seedcake.libraries.extension

import android.content.Context
import androidx.annotation.StringRes
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Context.createBiometricDialog(params: BiometricParams): BiometricPrompt.PromptInfo {

    fun BiometricPrompt.PromptInfo.Builder.setMessage() =
        params.messageTextRes?.let { setSubtitle(getString(it)) } ?: this

    val title = getString(params.titleTextRes)
    val appName = params.appNameTextRes?.let(::getString) ?: ""
    val cancelButton = getString(params.cancelButtonTextRes)

    return BiometricPrompt.PromptInfo
        .Builder()
        .setTitle("$title $appName")
        .setNegativeButtonText(cancelButton)
        .setMessage()
        .build()
}

data class BiometricParams(
    @StringRes val titleTextRes: Int,
    @StringRes val appNameTextRes: Int? = null,
    @StringRes val messageTextRes: Int? = null,
    @StringRes val cancelButtonTextRes: Int
)

fun Context.checkDeviceHasBiometric(block: (hasBiometric: Boolean) -> Unit) {
    val canAuthenticate = BiometricManager.from(this).run {
        canAuthenticate(Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL)
    }
    val hasBiometric = when (canAuthenticate) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }
    block(hasBiometric)
}

fun FragmentActivity.biometricListeners(
    errorAction: (Int) -> Unit = {},
    successAction: () -> Unit
): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(this)
    return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            errorAction(errorCode)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            successAction()
        }
    })
}

fun Fragment.biometricListeners(
    errorAction: (Int) -> Unit = {},
    successAction: () -> Unit
): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(requireContext())
    return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            errorAction(errorCode)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            successAction()
        }
    })
}
