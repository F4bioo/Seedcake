package com.fappslab.seedcake.libraries.design.pluto.fragment.progress

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoProgressDialogBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.isNotNull

class PlutoProgressDialog : DialogFragment(R.layout.pluto_progress_dialog) {

    private val binding: PlutoProgressDialogBinding by viewBinding()

    private var primaryButtonConfig: PlutoProgressDialogButtonConfig? = null
    private var primaryButtonAction: (() -> Unit)? = null
    var shouldShowMessage: Boolean = true

    var primaryButton: (PlutoProgressDialogButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                primaryButtonConfig = PlutoProgressDialogButtonConfig().apply(block)
                primaryButtonAction = primaryButtonConfig?.buttonAction
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupMessage()
        setupPrimaryButton()
    }

    override fun onResume() {
        isCancelable = false
        super.onResume()
    }

    private fun setupWindow() {
        dialog?.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun setupPrimaryButton() {
        binding.buttonPrimary.apply {
            isVisible = primaryButton.isNotNull()
        }.setOnClickListener {
            primaryButtonAction?.invoke()
        }
    }

    private fun setupMessage() {
        binding.textLoading.isVisible = shouldShowMessage
    }
}
