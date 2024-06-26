package com.fappslab.features.details.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.features.details.di.DetailsModuleLoad
import com.fappslab.features.details.presentation.extensions.numberedSeed
import com.fappslab.features.details.presentation.extensions.permissionLauncher
import com.fappslab.features.details.presentation.extensions.saveToGalleryAction
import com.fappslab.features.details.presentation.extensions.setPalletColors
import com.fappslab.features.details.presentation.extensions.showDeleteSeedModal
import com.fappslab.features.details.presentation.extensions.showDeniedStoragePermissionModal
import com.fappslab.features.details.presentation.extensions.showEditAliasModal
import com.fappslab.features.details.presentation.extensions.showFullEncryptedSeedModal
import com.fappslab.features.details.presentation.extensions.showInfoModal
import com.fappslab.features.details.presentation.extensions.showLoadingDialog
import com.fappslab.features.details.presentation.extensions.showUnlockSeedErrorDialog
import com.fappslab.features.details.presentation.extensions.showWhatSeeingDialog
import com.fappslab.features.details.presentation.viewmodel.DetailsViewAction
import com.fappslab.features.details.presentation.viewmodel.DetailsViewModel
import com.fappslab.features.details.presentation.viewmodel.DetailsViewState
import com.fappslab.features.details.presentation.viewmodel.UnlockParams
import com.fappslab.seedcake.features.details.R
import com.fappslab.seedcake.features.details.databinding.DetailsFragmentBinding
import com.fappslab.seedcake.features.details.databinding.DetailsModalEditBinding
import com.fappslab.seedcake.libraries.arch.dateformat.toDateFormatted
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.simplepermission.extension.openApplicationSettings
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.pluto.extension.plutoValidationDialog
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.args.putArgs
import com.fappslab.seedcake.libraries.extension.args.viewArgs
import com.fappslab.seedcake.libraries.extension.args.withArgs
import com.fappslab.seedcake.libraries.extension.copyToClipboard
import com.fappslab.seedcake.libraries.extension.isNotNull
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

internal class DetailsFragment : Fragment(R.layout.details_fragment), KoinLazy {

    private val binding: DetailsFragmentBinding by viewBinding()
    private val viewModel: DetailsViewModel by activityViewModel { parametersOf(args) }
    private val args: DetailsArgs by viewArgs()
    private val modalEditBinding by lazy { DetailsModalEditBinding.inflate(layoutInflater) }
    private val permissionLauncher = permissionLauncher { viewModel.onPermission(result = it) }

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(DetailsModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupToolbar()
        setupListeners()
    }

    override fun onPause() {
        viewModel.onEye()
        super.onPause()
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            imageQrcodeState(state)
            setupViewState(state.args)
            showLoadingDialog(state.shouldShowProgressDialog)
            showDeleteSeedModalState(state.shouldShowDeleteSeedModal)
            showDeniedPermissionModalState(state.shouldShowDeniedPermissionModal)
            binding.includeSeed.checkEye.isChecked = state.isEyeChecked
            showInfoModalState(state.shouldShowInfoModal)
            flipperChildState(state.childPosition)
            state.showDecryptErrorDialogState()
            state.showFullEncryptedSeedModalState()
            state.showEditAliasModalState()
        }

        onViewAction(viewModel) { action ->
            when (action) {
                DetailsViewAction.CloseSeed -> closeSeedAction()
                DetailsViewAction.FinishView -> activity?.finish()
                DetailsViewAction.Validation -> showValidationDialogAction()
                DetailsViewAction.RequestPermission -> requestPermissionAction()
                DetailsViewAction.GrantedPermission -> saveToGalleryAction()
                DetailsViewAction.WhatSeeing -> showWhatSeeingDialog()
                DetailsViewAction.OpenAppSettings -> context?.openApplicationSettings()
                is DetailsViewAction.UnlockedSeed -> unlockedSeedAction(action.params)
                is DetailsViewAction.Copy -> context?.copyToClipboard(data = action.encryptedSeed)
                is DetailsViewAction.SaveToGalleryResult -> saveToGalleryResultAction(action.message)
            }
        }
    }

    private fun setupToolbar() {
        binding.detailsToolbar.run {
            setNavigationOnClickListener { viewModel.onBack() }
            setOnMenuItemClickListener {
                viewModel.onDeleteVisibility(shouldShow = true)
                true
            }
        }
    }

    private fun setupListeners() = binding.run {
        includeSeed.checkEye.setOnCheckedChangeListener { _, isChecked -> viewModel.onEye(isChecked) }
        buttonDecrypt.setOnClickListener { viewModel.onValidationDialog() }
        imageQrcode.setOnClickListener { viewModel.onCopy() }
        textEncryptedSeed.setOnClickListener { viewModel.onCopy() }
        buttonShow.setOnClickListener { viewModel.onFullEncryptedSeedVisibility() }
        includeSeed.buttonInfo.setOnClickListener { viewModel.onInfoVisibility() }
        buttonEdit.setOnClickListener { viewModel.onEditAliasVisibility() }
        buttonClose.setOnClickListener { viewModel.onCloseSeed() }
    }

    private fun setupViewState(args: DetailsArgs) = binding.run {
        textEncryptedDate.text = args.date.toDateFormatted()
        textEncryptedSeed.text = args.unreadableSeedPhrase
        textAlias.text = args.alias
    }

    private fun imageQrcodeState(state: DetailsViewState) = binding.run {
        imageQrcode.isVisible = state.encryptedSeedBitmap.isNotNull()
        imageQrcode.setImageBitmap(state.encryptedSeedBitmap)
    }

    private fun showValidationDialogAction() {
        context?.plutoValidationDialog(viewModel::onUnlockSeed)
    }

    private fun showDeleteSeedModalState(shouldShow: Boolean) {
        showDeleteSeedModal(
            shouldShow,
            viewModel::onDeleteVisibility,
            viewModel::onDeleteConfirmation
        )
    }

    private fun DetailsViewState.showFullEncryptedSeedModalState() {
        showFullEncryptedSeedModal(
            shouldShowFullEncryptedSeedModal,
            args.unreadableSeedPhrase,
            viewModel::onFullEncryptedSeedVisibility,
            viewModel::onCopy,
            viewModel::onWhatSeeing
        )
    }

    private fun showInfoModalState(shouldShow: Boolean) {
        showInfoModal(
            shouldShow,
            viewModel::onInfoVisibility,
            viewModel::onSaveToGallery
        )
    }

    private fun DetailsViewState.showDecryptErrorDialogState() {
        showUnlockSeedErrorDialog(
            dialogErrorPair,
            shouldShowDecryptErrorModal,
            viewModel::onDecryptErrorVisibility
        )
    }

    private fun DetailsViewState.showEditAliasModalState() {
        modalEditBinding.inputAlias.setText(args.alias)
        showEditAliasModal(
            modalEditBinding,
            shouldShowEditAliasModal,
            viewModel::onEditAliasVisibility,
            viewModel::onSave
        )
    }

    private fun flipperChildState(childPosition: Int) = binding.run {
        includeSeed.flipperContainer.displayedChild = childPosition
    }

    private fun showDeniedPermissionModalState(shouldShow: Boolean) {
        showDeniedStoragePermissionModal(
            shouldShow,
            viewModel::onDeniedPermissionVisibility,
            viewModel::onOpenAppSettings
        )
    }

    private fun closeSeedAction() {
        binding.run {
            buttonClose.isVisible = false
            includeSeed.seedContainer.isVisible = false
        }
    }

    private fun unlockedSeedAction(params: UnlockParams) {
        binding.run {
            buttonClose.isVisible = true
            includeSeed.seedContainer.isVisible = true
            includeSeed.setPalletColors(params.coloredSeed)
            includeSeed.textReadableSeed.numberedSeed(params.decryptedSeed)
        }
    }

    private fun saveToGalleryResultAction(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveToGalleryAction() {
        binding.includeSeed.coloredSeedContainer
            .saveToGalleryAction(viewModel::onSaveToGalleryResult)
    }

    private fun requestPermissionAction() {
        permissionLauncher?.requestPermission() ?: saveToGalleryAction()
    }

    companion object {
        fun createFragment(args: DetailsArgs): Fragment =
            DetailsFragment().withArgs { putArgs(args) }
    }
}
