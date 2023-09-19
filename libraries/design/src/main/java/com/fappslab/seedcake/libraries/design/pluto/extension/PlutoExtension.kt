package com.fappslab.seedcake.libraries.design.pluto.extension

import android.content.Context
import android.view.LayoutInflater
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoValidationDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.plutoValidationDialog(primaryBlock: (String) -> Unit) {
    val inflater = LayoutInflater.from(this)
    val binding = PlutoValidationDialogBinding.inflate(inflater)

    MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.passphrase_for_decryption))
        .setCancelable(false)
        .setView(binding.root)
        .setNegativeButton(R.string.common_cancel, null)
        .setPositiveButton(R.string.common_continue) { dialog, _ ->
            val text = binding.inputValidation.text
            primaryBlock(text.toString())
            dialog.dismiss()
        }.show()
}

