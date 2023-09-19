package com.fappslab.features.decrypt.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.fappslab.features.decrypt.di.DecryptModuleLoad
import com.fappslab.features.decrypt.presentation.extension.onAfterTextChanged
import com.fappslab.features.decrypt.presentation.extension.permissionLauncher
import com.fappslab.features.decrypt.presentation.extension.showDeniedCameraPermissionModal
import com.fappslab.features.decrypt.presentation.extension.showLoadingDialog
import com.fappslab.features.decrypt.presentation.extension.showUnlockSeedErrorModal
import com.fappslab.features.decrypt.presentation.model.PageArgs
import com.fappslab.features.decrypt.presentation.viewmodel.DecryptViewAction
import com.fappslab.features.decrypt.presentation.viewmodel.DecryptViewModel
import com.fappslab.features.decrypt.presentation.viewmodel.DecryptViewState
import com.fappslab.seedcake.features.decrypt.R
import com.fappslab.seedcake.features.decrypt.databinding.FragmentPageBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.simplepermission.extension.openApplicationSettings
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.reader.PlutoQrcodeReaderActivity
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.reader.PlutoQrcodeReaderActivity.Companion.EXTRA_QRCODE_RESULT
import com.fappslab.seedcake.libraries.design.pluto.extension.plutoValidationDialog
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.activityLauncher
import com.fappslab.seedcake.libraries.extension.args.putArgs
import com.fappslab.seedcake.libraries.extension.args.viewArgs
import com.fappslab.seedcake.libraries.extension.args.withArgs
import com.fappslab.seedcake.libraries.extension.inputError
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

internal class PageFragment : Fragment(R.layout.fragment_page), KoinLazy {

    private val binding: FragmentPageBinding by viewBinding()
    private val viewModel: DecryptViewModel by viewModel { parametersOf(args.pageType) }
    private val args: PageArgs by viewArgs()
    private val permissionLauncher = permissionLauncher { status ->
        viewModel.onPermission(status)
    }
    private val activityLauncher = activityLauncher { data ->
        data?.getStringExtra(EXTRA_QRCODE_RESULT)?.let {
            viewModel.onScannerResult(encryptedSeed = it)
        }
    }

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(DecryptModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupListeners()
        setupView()
    }

    override fun onPause() {
        viewModel.onEye()
        super.onPause()
    }

    override fun onDestroyView() {
        viewModel.onClear()
        activityLauncher.unregister()
        super.onDestroyView()
    }

    private fun setupObservables() = binding.run {
        onViewState(viewModel) { state ->
            checkEye.isChecked = state.isEyeChecked
            inputLayoutUnreadableSeed.inputError(state.inputSeedErrorRes)
            inputLayoutUnreadableSeed.setEndIconDrawable(state.endIconRes)
            showDeniedPermissionModalState(state.shouldShowDeniedPermissionModal)
            showLoadingDialog(state.shouldShowProgressDialog)
            flipperChildState(state.childPosition)
            state.showUnlockSeedErrorModalState()
        }

        onViewAction(viewModel) { action ->
            when (action) {
                DecryptViewAction.Validation -> showValidationDialogAction()
                DecryptViewAction.ClearInput -> inputUnreadableSeed.text?.clear()
                DecryptViewAction.RequestPermission -> permissionLauncher.requestPermission()
                DecryptViewAction.GrantedPermission -> navigateToActivityScannerAction()
                DecryptViewAction.OpenAppSettings -> context?.openApplicationSettings()
                is DecryptViewAction.ScannerResult -> inputUnreadableSeed.setText(action.unreadable)
                is DecryptViewAction.UnlockedSeed -> unlockedSeed(action.readableSeed)
            }
        }
    }

    private fun setupListeners() = binding.run {
        inputUnreadableSeed.onAfterTextChanged(viewModel::onTextChangedUnreadableSeed)
        checkEye.setOnCheckedChangeListener { _, isChecked -> viewModel.onEye(isChecked) }
        inputLayoutUnreadableSeed.setEndIconOnClickListener { viewModel.onEndIcon() }
        buttonPrimary.setOnClickListener { viewModel.onUnlockSeed() }
    }

    private fun setupView() = binding.run {
        inputLayoutUnreadableSeed.placeholderText = getString(args.placeholderTextRes)
        inputLayoutUnreadableSeed.hint = getString(args.hintTextRes)
        textEyeDescription.text = getString(args.eyeDescriptionTextRes)
        buttonPrimary.text = getString(args.primaryButtonTextRes)
    }

    private fun flipperChildState(childPosition: Int) = binding.run {
        flipperContainer.displayedChild = childPosition
    }

    private fun showDeniedPermissionModalState(shouldShow: Boolean) {
        showDeniedCameraPermissionModal(
            shouldShow,
            viewModel::onDeniedPermissionVisibility,
            viewModel::onOpenAppSettings
        )
    }

    private fun DecryptViewState.showUnlockSeedErrorModalState() {
        showUnlockSeedErrorModal(
            inputPassErrorRes,
            shouldShowUnlockSeedErrorModal,
            viewModel::onUnlockSeedErrorVisibility
        )
    }

    private fun navigateToActivityScannerAction() = context?.let {
        PlutoQrcodeReaderActivity.createIntent(context = it)
            .also(activityLauncher::launch)
    }

    private fun showValidationDialogAction() {
        context?.plutoValidationDialog(viewModel::onDecryptSeed)
    }

    private fun unlockedSeed(readableSeed: String) = binding.run {
        textReadableSeed.text = readableSeed
        seedContainer.isVisible = true
    }

    companion object {
        fun createFragment(args: PageArgs): Fragment =
            PageFragment().withArgs { putArgs(args) }
    }
}
