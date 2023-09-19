package com.fappslab.seedcake.libraries.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast

fun Context.copyToClipboard(label: String? = null, data: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText(label, data)
    clipboard?.setPrimaryClip(clip)

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        if (data == clipboard?.primaryClip?.getItemAt(0)?.text) {
            val message = getString(R.string.copied_to_clipboard)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
