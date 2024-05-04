package com.fappslab.features.encrypt.secret.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fappslab.features.encrypt.secret.domain.usecase.MIN_PASSPHRASE_LENGTH
import com.fappslab.features.encrypt.secret.presentation.extension.getStrengthColor
import com.fappslab.features.encrypt.secret.presentation.extension.rotateButton
import com.fappslab.features.encrypt.secret.presentation.viewmodel.passphrase.GeneratorViewAction
import com.fappslab.features.encrypt.secret.presentation.viewmodel.passphrase.GeneratorViewModel
import com.fappslab.libraries.security.model.SecureString
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptGeneratorBottomSheetBinding
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.pluto.extension.plutoFeedbackChoiceDialog
import com.fappslab.seedcake.libraries.extension.copyToClipboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal const val TAG_GENERATOR_MODAL = "GeneratorBottomSheet"

internal class GeneratorBottomSheet : BottomSheetDialogFragment() {

    private var _binding: EncryptGeneratorBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GeneratorViewModel by viewModel()

    var onUseClicked: ((SecureString) -> Unit)? = null
    var shouldBlock: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EncryptGeneratorBottomSheetBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onGeneratePassphrase(MIN_PASSPHRASE_LENGTH)
        setupObservables()
        setupListeners()
        setupBehavior()
    }

    override fun getTheme(): Int {
        return R.style.PlutoBottomSheet
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservables() = binding.run {
        onViewState(viewModel) { state ->
            sliderSize.value = state.sliderSizeValue.toFloat()
        }

        onViewAction(viewModel) { action ->
            when (action) {
                GeneratorViewAction.RandomButton -> randomButtonAction()
                GeneratorViewAction.CloseButton -> dismissAllowingStateLoss()
                is GeneratorViewAction.UseButton -> useButtonAction(action.secure)
                is GeneratorViewAction.Generated -> generatedAction(action.secure)
                is GeneratorViewAction.WarningCopy -> warningCopyAction(action.secure)
                is GeneratorViewAction.Copy -> copyAction(action.secure)
            }
        }
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

    private fun setupListeners() = binding.run {
        sliderSize.addOnChangeListener { _, value, _ ->
            viewModel.onGeneratePassphrase(value.toInt())
        }

        buttonRandom.setOnClickListener {
            viewModel.onRandomGenerate(sliderSize.value.toInt())
        }

        buttonClose.setOnClickListener {
            viewModel.onClose()
        }

        buttonUse.setOnClickListener {
            SecureString.from(textPassphrase.text.toString())
                .also(viewModel::onUse)
        }

        buttonCopy.setOnClickListener {
            SecureString.from(textPassphrase.text.toString())
                .also(viewModel::onWarningCopy)
        }

        buttonDecrease.setOnClickListener {
            viewModel.onDecreaseLength(sliderSize.value.toInt())
        }

        buttonIncrease.setOnClickListener {
            viewModel.onIncreaseLength(sliderSize.value.toInt())
        }
    }

    private fun useButtonAction(securePassphrase: SecureString) {
        onUseClicked?.invoke(securePassphrase)
        dismissAllowingStateLoss()
    }

    private fun randomButtonAction() = binding.run {
        buttonRandom.rotateButton()
    }

    private fun generatedAction(securePassphrase: SecureString) = binding.run {
        val length = sliderSize.value.toInt()
        cardContainer.strokeColor = getStrengthColor(length)
        textTitleSize.text = getString(R.string.encrypt_characters_size, length)
        securePassphrase.use { textPassphrase.text = String(it) }
    }

    private fun copyAction(securePassphrase: SecureString) {
        securePassphrase.use { context?.copyToClipboard(data = String(it)) }
    }

    private fun warningCopyAction(securePassphrase: SecureString) {
        context?.plutoFeedbackChoiceDialog { viewModel.onCopyToClipboard(securePassphrase) }
    }

    companion object {
        fun createFragment(
            block: GeneratorBottomSheet.() -> Unit
        ): GeneratorBottomSheet = GeneratorBottomSheet().apply(block)
    }
}
