package com.fappslab.seedcake.libraries.design.pluto.fragment.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoFeedbackDialogBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding

class PlutoFeedbackDialog : DialogFragment(R.layout.pluto_feedback_dialog) {

    private val binding: PlutoFeedbackDialogBinding by viewBinding()

    @StringRes
    var titleRes: Int? = null
    var titleText: String? = null

    @StringRes
    var messageRes: Int? = null
    var messageText: String? = null

    private var primaryButtonConfig: PlutoFeedbackDialogButtonConfig? = null
    private var primaryButtonAction: (() -> Unit)? = null
    var gravityDialog: GravityType = GravityType.Bottom
    var onDismiss: (() -> Unit)? = null
    var shouldBlock: Boolean = true

    var primaryButton: (PlutoFeedbackDialogButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                primaryButtonConfig = PlutoFeedbackDialogButtonConfig().apply(block)
                primaryButtonAction = primaryButtonConfig?.buttonAction
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupBehavior()
        setupTitle()
        setupMessage()
        setupPrimaryButton()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    private fun setupWindow() {
        dialog?.window?.run {
            gravityBottom(gravityDialog)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun setupBehavior() {
        isCancelable = shouldBlock.not()
    }

    private fun setupTitle() {
        binding.textTitle.apply {
            text = getText(titleRes, titleText)
            handleVisibility()
        }
    }

    private fun setupMessage() {
        binding.textMessage.apply {
            text = getText(messageRes, messageText)
            movementMethod = ScrollingMovementMethod()
            handleVisibility()
        }
    }

    private fun setupPrimaryButton() {
        binding.buttonPrimary.setOnClickListener {
            primaryButtonAction?.invoke()
        }
    }

    private fun TextView.handleVisibility() {
        isVisible = text.isNotEmpty()
    }

    private fun getText(contentRes: Int?, messageText: String?): String {
        contentRes?.let { return getString(it) }
        return messageText.orEmpty()
    }

    private fun Window.gravityBottom(gravityType: GravityType) {
        val gravity = if (gravityType == GravityType.Bottom) {
            Gravity.BOTTOM
        } else Gravity.CENTER

        setGravity(gravity)
    }
}

enum class GravityType {
    Center,
    Bottom
}
