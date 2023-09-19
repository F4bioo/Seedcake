package com.fappslab.seedcake.libraries.design.pluto.fragment.lock

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoLockPageBinding
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.args.putArgs
import com.fappslab.seedcake.libraries.extension.args.viewArgs
import com.fappslab.seedcake.libraries.extension.args.withArgs

class PlutoLockPage : DialogFragment(R.layout.pluto_lock_page) {

    private val binding: PlutoLockPageBinding by viewBinding()
    private val args: ScreenType by viewArgs()
    var onDismiss: (() -> Unit)? = null
    var onBackPressed: (() -> Unit)? = null
    var onBiometricButton: (() -> Unit)? = null
    var onValidationResult: ((PinResult) -> Unit)? = null
    var shouldFinishAppOnBack = false
    var shouldShowBiometricButton = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    if (shouldFinishAppOnBack) {
                        activity?.finishAffinity()
                    } else dismissAllowingStateLoss()
                    onBackPressed?.invoke()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getTheme(): Int {
        return R.style.PlutoFullScreenDialogStyle
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    private fun setupView() = binding.run {
        inputPin.setPinType(args)
        textPinLockWarning.isVisible = args is ScreenType.Register
        inputPin.validation = { result -> onValidationResult?.invoke(result) }
        buttonBiometric.setOnClickListener { onBiometricButton?.invoke() }
        buttonBiometric.isVisible = shouldShowBiometricButton
    }

    companion object {
        fun createFragment(args: ScreenType): PlutoLockPage =
            PlutoLockPage().withArgs { putArgs(args) }
    }
}
