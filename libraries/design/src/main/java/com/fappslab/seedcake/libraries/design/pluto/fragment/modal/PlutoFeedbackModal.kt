package com.fappslab.seedcake.libraries.design.pluto.fragment.modal

import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.databinding.PlutoFeedbackModalBinding
import com.fappslab.seedcake.libraries.extension.isNotNull
import com.fappslab.seedcake.libraries.extension.isNull
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlutoFeedbackModal : BottomSheetDialogFragment() {

    private var _binding: PlutoFeedbackModalBinding? = null
    private val binding get() = _binding!!

    private var primaryButtonConfig: PlutoFeedbackModalButtonConfig? = null
    private var secondaryButtonConfig: PlutoFeedbackModalButtonConfig? = null
    private var tertiaryButtonConfig: PlutoFeedbackModalButtonConfig? = null
    private var primaryButtonAction: (() -> Unit)? = null
    private var secondaryButtonAction: (() -> Unit)? = null
    private var tertiaryButtonAction: (() -> Unit)? = null

    var customView: View? = null
    var closeButton: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null
    var shouldBlock: Boolean = true

    @DrawableRes
    var imageRes: Int? = null

    @StringRes
    var titleRes: Int? = null
    var titleText: String? = null

    @StringRes
    var messageRes: Int? = null
    var messageText: String? = null
    var messageSpanned: Spanned? = null

    var primaryButton: (PlutoFeedbackModalButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                primaryButtonConfig = PlutoFeedbackModalButtonConfig().apply(block)
                primaryButtonAction = primaryButtonConfig?.buttonAction
            }
        }

    var secondaryButton: (PlutoFeedbackModalButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                secondaryButtonConfig = PlutoFeedbackModalButtonConfig().apply(block)
                secondaryButtonAction = secondaryButtonConfig?.buttonAction
            }
        }

    var tertiaryButton: (PlutoFeedbackModalButtonConfig.() -> Unit)? = null
        set(value) {
            field = value
            value?.let { block ->
                tertiaryButtonConfig = PlutoFeedbackModalButtonConfig().apply(block)
                tertiaryButtonAction = tertiaryButtonConfig?.buttonAction
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlutoFeedbackModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        primaryButtonConfig?.setupPrimaryButton()
        secondaryButtonConfig?.setupSecondaryButton()
        tertiaryButtonConfig?.setupTertiaryButton()
        setupButtonContainer()
        setupBehavior()
        setupDragLine()
        setupCloseButton()
        setupAvatarImage()
        setupTitle()
        setupMessage()
        setupCustomView()
    }

    override fun getTheme(): Int {
        return R.style.PlutoBottomSheet
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
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            saveFlags = BottomSheetBehavior.SAVE_ALL
            state = BottomSheetBehavior.STATE_EXPANDED
            if (shouldBlock) {
                isHideable = true
                isCancelable = false
                isDraggable = false
            }
        }
    }

    private fun setupDragLine() {
        binding.dragLine.isInvisible = shouldBlock
    }

    private fun setupCloseButton() {
        binding.buttonClose.setOnClickListener {
            closeButton?.invoke()
            dismissAllowingStateLoss()
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

    private fun PlutoFeedbackModalButtonConfig.setupPrimaryButton() {
        binding.buttonPrimary.apply {
            buttonTextRes?.let(::setText)
            buttonText?.let(::setText)
            setOnClickListener { primaryButtonAction?.invoke() }
            isVisible = primaryButton.isNotNull()
        }
    }

    private fun PlutoFeedbackModalButtonConfig.setupSecondaryButton() {
        binding.buttonSecondary.apply {
            buttonTextRes?.let(::setText)
            buttonText?.let(::setText)
            setOnClickListener { secondaryButtonAction?.invoke() }
            isVisible = secondaryButton.isNotNull()
        }
    }

    private fun PlutoFeedbackModalButtonConfig.setupTertiaryButton() {
        binding.buttonTertiary.apply {
            buttonTextRes?.let(::setText)
            buttonText?.let(::setText)
            setOnClickListener { tertiaryButtonAction?.invoke() }
            isVisible = tertiaryButton.isNotNull()
        }
    }
}
