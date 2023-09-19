package com.fappslab.seedcake.libraries.design.pluto.fragment.page

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoFeedbackPageBinding
import com.fappslab.seedcake.libraries.extension.isNotNull
import com.fappslab.seedcake.libraries.extension.isNull

class PlutoFeedbackPage : DialogFragment() {

    private var _binding: PlutoFeedbackPageBinding? = null
    private val binding get() = _binding!!

    private var primaryButtonConfig: PlutoFeedbackPageButtonConfig? = null
    private var secondaryButtonConfig: PlutoFeedbackPageButtonConfig? = null
    private var tertiaryButtonConfig: PlutoFeedbackPageButtonConfig? = null
    private var primaryButtonAction: (() -> Unit)? = null
    private var secondaryButtonAction: (() -> Unit)? = null
    private var tertiaryButtonAction: (() -> Unit)? = null

    var customView: View? = null
    var closeButton: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null
    var onBackPressed: (() -> Unit)? = null
    var shouldBlock: Boolean = true
    var allowFinishParentActivity = false

    @DrawableRes
    var imageRes: Int? = null

    @StringRes
    var titleRes: Int? = null
    var titleText: String? = null

    @StringRes
    var messageRes: Int? = null
    var messageText: String? = null
    var messageSpanned: Spanned? = null

    var primaryButton: (PlutoFeedbackPageButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                primaryButtonConfig = PlutoFeedbackPageButtonConfig().apply(block)
                primaryButtonAction = primaryButtonConfig?.buttonAction
            }
        }

    var secondaryButton: (PlutoFeedbackPageButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                secondaryButtonConfig = PlutoFeedbackPageButtonConfig().apply(block)
                secondaryButtonAction = secondaryButtonConfig?.buttonAction
            }
        }

    var tertiaryButton: (PlutoFeedbackPageButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                tertiaryButtonConfig = PlutoFeedbackPageButtonConfig().apply(block)
                tertiaryButtonAction = tertiaryButtonConfig?.buttonAction
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlutoFeedbackPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    if (allowFinishParentActivity) activity?.finish()
                    onBackPressed?.invoke()
                    dismissAllowingStateLoss()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        primaryButtonConfig?.setupPrimaryButton()
        secondaryButtonConfig?.setupSecondaryButton()
        tertiaryButtonConfig?.setupTertiaryButton()
        setupButtonContainer()
        setupBehavior()
        setupToolbar()
        setupAvatarImage()
        setupTitle()
        setupMessage()
        setupCustomView()
    }

    override fun getTheme(): Int {
        return R.style.PlutoFullScreenDialogStyle
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBehavior() {
        if (shouldBlock) isCancelable = false
    }

    private fun setupToolbar() {
        binding.toolbarPage.run {
            setOnMenuItemClickListener {
                closeButton?.invoke()
                dismissAllowingStateLoss()
                true
            }
        }
    }

    private fun setupAvatarImage() {
        binding.imageAvatar.run {
            imageRes?.let(::setImageResource)
            isVisible = imageRes.isNotNull()
        }
        if (imageRes.isNull()) {
            binding.textTile.run {
                val layoutParams = this.layoutParams as? ViewGroup.MarginLayoutParams
                layoutParams?.topMargin = 0
                this.layoutParams = layoutParams
            }
        }
    }

    private fun setupTitle() {
        binding.textTile.run {
            titleRes?.let(::setText)
            titleText?.let(::setText)
            isVisible = text.isNotEmpty()
        }
    }

    private fun setupMessage() {
        binding.textMessage.run {
            messageRes?.let(::setText)
            messageText?.let(::setText)
            messageSpanned?.let(::setText)
            isVisible = text.isNotEmpty()
        }
    }

    private fun setupCustomView() = binding.run {
        customView?.let { customView ->
            val parentViewGroup = customView.parent as? ViewGroup
            parentViewGroup?.removeView(customView)
            customContainer.addView(customView)
            customContainer.isVisible = true
            scrollContainer.isVisible = false
        }
    }

    private fun setupButtonContainer() {
        binding.buttonContainer.isVisible =
            primaryButton.isNotNull() ||
                    secondaryButton.isNotNull() ||
                    tertiaryButton.isNotNull()
    }

    private fun PlutoFeedbackPageButtonConfig.setupPrimaryButton() {
        binding.buttonPrimary.apply {
            buttonTextRes?.let(::setText)
            buttonText?.let(::setText)
            setOnClickListener { primaryButtonAction?.invoke() }
            isVisible = primaryButton.isNotNull()
        }
    }

    private fun PlutoFeedbackPageButtonConfig.setupSecondaryButton() {
        binding.buttonSecondary.apply {
            buttonTextRes?.let(::setText)
            buttonText?.let(::setText)
            setOnClickListener { secondaryButtonAction?.invoke() }
            isVisible = secondaryButton.isNotNull()
        }
    }

    private fun PlutoFeedbackPageButtonConfig.setupTertiaryButton() {
        binding.buttonTertiary.apply {
            buttonTextRes?.let(::setText)
            buttonText?.let(::setText)
            setOnClickListener { tertiaryButtonAction?.invoke() }
            isVisible = tertiaryButton.isNotNull()
        }
    }
}
