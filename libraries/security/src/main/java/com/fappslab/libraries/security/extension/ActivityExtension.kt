package com.fappslab.libraries.security.extension

import android.app.Activity
import android.view.WindowManager


fun Activity.screenShield(isEnabled: Boolean) {
    if (isEnabled) window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    ) else window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}
