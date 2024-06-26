package com.fappslab.features.encrypt.result.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fappslab.features.encrypt.main.presentation.REQUEST_KEY_PROGRESS
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress4
import com.fappslab.features.encrypt.result.di.ResultModuleLoad
import com.fappslab.features.encrypt.result.presentation.extension.showFullEncryptedSeedModal
import com.fappslab.features.encrypt.result.presentation.extension.showSaveErrorModal
import com.fappslab.features.encrypt.result.presentation.extension.showSaveToast
import com.fappslab.features.encrypt.result.presentation.extension.showWhatSeeingDialog
import com.fappslab.features.encrypt.result.presentation.viewmodel.ResultViewAction
import com.fappslab.features.encrypt.result.presentation.viewmodel.ResultViewModel
import com.fappslab.features.encrypt.result.presentation.viewmodel.ResultViewState
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentResultBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.addBackPressedCallback
import com.fappslab.seedcake.libraries.extension.copyToClipboard
import com.fappslab.seedcake.libraries.extension.isNotNull
import com.fappslab.seedcake.libraries.extension.setFragmentResult
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

internal class ResultFragment : Fragment(R.layout.encrypt_fragment_result), KoinLazy {

    private val binding: EncryptFragmentResultBinding by viewBinding()
    private val viewModel: ResultViewModel by viewModel { parametersOf(args.result) }
    private val args: ResultFragmentArgs by navArgs()
    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(ResultModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressed()
        setupObservables()
        setupListeners()
        setupView()
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            saveButtonState(state.shouldEnableSaveButton)
            progressStepState(state.progress)
            imageQrcodeState(state)
            showSaveErrorModalState(state)
            state.showFullEncryptedSeedModalState()
        }

        onViewAction(viewModel) { action ->
            when (action) {
                ResultViewAction.FinishView -> activity?.finish()
                ResultViewAction.BackPressed -> backPressedAction()
                ResultViewAction.WhatSeeing -> showWhatSeeingDialog()
                ResultViewAction.SaveSuccess -> context?.showSaveToast()
                is ResultViewAction.Copy -> context?.copyToClipboard(data = action.encryptedSeed)
            }
        }
    }

    private fun setupListeners() = binding.run {
        imageQrcode.setOnClickListener { viewModel.onCopy() }
        textEncryptedSeed.setOnClickListener { viewModel.onCopy() }
        buttonShow.setOnClickListener { viewModel.onFullEncryptedSeedVisibility() }
        buttonSave.setOnClickListener { viewModel.onSave() }
        buttonClose.setOnClickListener { viewModel.onFinishView() }
    }

    private fun setupBackPressed() {
        addBackPressedCallback {
            viewModel.onBackPressed()
        }
    }

    private fun setupView() = binding.run {
        textAlias.text = args.result.alias
        textEncryptedSeed.text = args.result.encryptedSeed
    }

    private fun progressStepState(progress: Int) {
        setFragmentResult(REQUEST_KEY_PROGRESS, pair = Progress4.name to progress)
    }

    private fun saveButtonState(isEnabled: Boolean) {
        binding.buttonSave.isEnabled = isEnabled
    }

    private fun imageQrcodeState(state: ResultViewState) = binding.run {
        imageQrcode.isVisible = state.encryptedSeedBitmap.isNotNull()
        imageQrcode.setImageBitmap(state.encryptedSeedBitmap)
    }

    private fun ResultViewState.showFullEncryptedSeedModalState() {
        showFullEncryptedSeedModal(
            shouldShowFullEncryptedSeedModal,
            args.encryptedSeed,
            viewModel::onFullEncryptedSeedVisibility,
            viewModel::onCopy,
            viewModel::onWhatSeeing
        )
    }

    private fun showSaveErrorModalState(state: ResultViewState) {
        showSaveErrorModal(
            state.shouldShowSaveErrorModal,
            viewModel::onSaveErrorVisibilityModal,
            viewModel::onTryAgain,
            viewModel::onFinishView
        )
    }

    private fun backPressedAction() {
        findNavController().popBackStack(R.id.disclaimerFragment, false)
    }
}
