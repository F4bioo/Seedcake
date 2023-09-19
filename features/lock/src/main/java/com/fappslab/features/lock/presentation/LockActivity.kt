package com.fappslab.features.lock.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.fappslab.features.common.navigation.EXTRA_LOCK_RESULT
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.features.lock.di.LockModuleLoad
import com.fappslab.features.lock.presentation.extension.createBiometricDialog
import com.fappslab.features.lock.presentation.viewmodel.LockViewAction
import com.fappslab.features.lock.presentation.viewmodel.LockViewModel
import com.fappslab.seedcake.features.lock.R
import com.fappslab.seedcake.features.lock.databinding.ActivityLockBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.addBackPressedCallback
import com.fappslab.seedcake.libraries.extension.args.putArgs
import com.fappslab.seedcake.libraries.extension.args.toIntent
import com.fappslab.seedcake.libraries.extension.args.viewArgs
import com.fappslab.seedcake.libraries.extension.biometricListeners
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class LockActivity : AppCompatActivity(R.layout.activity_lock), KoinLazy {

    private val binding: ActivityLockBinding by viewBinding(R.id.lock_root)
    private val viewModel: LockViewModel by viewModel { parametersOf(args) }
    private val args: ScreenTypeArgs by viewArgs()
    private val prompt by lazy { createBiometricDialog() }
    private val biometricPrompt by lazy {
        biometricListeners(successAction = viewModel::onSuccessBiometricValidation)
    }

    override val scope: Scope by activityScope()

    override val koinLoad: KoinLoad by subModules(LockModuleLoad)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservables()
        setupListeners()
        setupBackPressed()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onShowLockScreen()
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            screenTypeState(state.screenType)
            pinLockWarningState(state.shouldShowPinLockWarning)
            buttonBiometricState(state.shouldShowFingerPrintButton)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                LockViewAction.FinishView -> finish()
                LockViewAction.FinishApp -> finishAffinity()
                LockViewAction.ShowBiometric -> prompt.let(biometricPrompt::authenticate)
                is LockViewAction.Preferences -> successValidationAction(action.result)
            }
        }
    }

    private fun setupListeners() = binding.run {
        buttonBiometric.setOnClickListener { viewModel.onShowBiometric() }
        inputPin.validation = { viewModel.onSuccessPinValidation(result = it) }
    }

    private fun setupBackPressed() {
        addBackPressedCallback {
            viewModel.onBackPressed()
        }
    }

    private fun screenTypeState(screenType: ScreenType?) {
        screenType?.let(binding.inputPin::setPinType)
    }

    private fun buttonBiometricState(shouldShow: Boolean) {
        binding.buttonBiometric.isVisible = shouldShow
    }

    private fun pinLockWarningState(shouldShow: Boolean) {
        binding.textPinLockWarning.isVisible = shouldShow
    }

    private fun successValidationAction(result: PinResult) {
        val data = Intent()
        data.putExtra(EXTRA_LOCK_RESULT, result)
        setResult(RESULT_OK, data)
        finish()
    }

    companion object {
        fun createIntent(context: Context, args: ScreenTypeArgs): Intent {
            return context.toIntent<LockActivity>().putArgs(args)
        }
    }
}
