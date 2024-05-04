package com.fappslab.features.preferences.presentation

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import com.fappslab.features.common.navigation.EXTRA_LOCK_RESULT
import com.fappslab.features.common.navigation.LockNavigation
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.features.preferences.di.PreferencesModuleLoad
import com.fappslab.features.preferences.presentation.extension.createBiometricDialog
import com.fappslab.features.preferences.presentation.viewmodel.PreferencesViewAction
import com.fappslab.features.preferences.presentation.viewmodel.PreferencesViewModel
import com.fappslab.libraries.security.extension.screenShield
import com.fappslab.seedcake.core.data.local.BuildConfig
import com.fappslab.seedcake.features.preferences.R
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult
import com.fappslab.seedcake.libraries.extension.activityLauncher
import com.fappslab.seedcake.libraries.extension.args.getParcelableCompat
import com.fappslab.seedcake.libraries.extension.biometricListeners
import com.fappslab.seedcake.libraries.extension.checkDeviceHasBiometric
import com.fappslab.seedcake.libraries.extension.orFalse
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

internal class PreferencesFragment : PreferenceFragmentCompat(), KoinLazy {

    private val viewModel: PreferencesViewModel by viewModel()
    private val lockNavigation: LockNavigation by inject()
    private var checkBoxPin: CheckBoxPreference? = null
    private var checkBoxShufflePin: CheckBoxPreference? = null
    private var checkBoxFingerprint: CheckBoxPreference? = null
    private var checkBoxScreenShield: CheckBoxPreference? = null
    private val prompt by lazy { context?.createBiometricDialog() }
    private val biometricPrompt by lazy {
        biometricListeners(viewModel::onBiometricError, viewModel::onBiometricSuccess)
    }
    private val activityLauncher = activityLauncher { data ->
        data?.getParcelableCompat(EXTRA_LOCK_RESULT, PinResult::class.java)?.let {
            viewModel.onLockResult(result = it)
        }
    }

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(PreferencesModuleLoad)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = BuildConfig.SIMPLE_PREFS_FILE_NAME
        setPreferencesFromResource(R.xml.preferences, rootKey)
        checkBoxPin = findPreference(BuildConfig.SIMPLE_PREFS_PIN_KEY)
        checkBoxFingerprint = findPreference(BuildConfig.SIMPLE_PREFS_FINGERPRINT_KEY)
        checkBoxScreenShield = findPreference(BuildConfig.SIMPLE_PREFS_SCREEN_SHIELD)
        checkBoxShufflePin = findPreference(BuildConfig.SIMPLE_PREFS_SHUFFLE_PIN)
        setupCheckBiometricAvailability()
        setupObservables()
        setupListeners()
    }

    override fun onDestroyView() {
        activityLauncher.unregister()
        super.onDestroyView()
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            checkBoxPin?.isChecked = state.isCheckBoxPinChecked
            checkBoxFingerprint?.isEnabled = state.isCheckBoxPinChecked
            checkBoxFingerprint?.isVisible = state.shouldShowCheckBoxFingerprint
            checkBoxFingerprint?.isChecked = state.isCheckBoxFingerprintChecked
            checkBoxShufflePin?.isChecked = state.isCheckBoxShufflePinChecked
            checkBoxShufflePin?.isEnabled = state.isCheckBoxPinChecked
        }

        onViewAction(viewModel) { action ->
            when (action) {
                PreferencesViewAction.BiometricPrompt -> prompt?.let(biometricPrompt::authenticate)
                is PreferencesViewAction.ScreenShield -> screenShieldAction(action.isChecked)
                is PreferencesViewAction.ShowLockScreen -> showLockScreenAction(action.args)
            }
        }
    }

    private fun setupListeners() {
        checkBoxPin?.setOnPreferenceChangeListener { _, _ ->
            viewModel.onShowLockScreen()
            false
        }
        checkBoxShufflePin?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onShufflePin((newValue as? Boolean).orFalse())
            false
        }
        checkBoxFingerprint?.setOnPreferenceChangeListener { _, _ ->
            viewModel.onBiometricPrompt()
            false
        }
        checkBoxScreenShield?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onScreenShield((newValue as? Boolean).orFalse())
            true
        }
    }

    private fun setupCheckBiometricAvailability() {
        context?.checkDeviceHasBiometric(viewModel::onCheckBiometricAvailability)
    }

    private fun screenShieldAction(isChecked: Boolean) {
        activity?.screenShield(isEnabled = isChecked)
    }

    private fun showLockScreenAction(args: ScreenTypeArgs) = context?.let {
        lockNavigation.createLockIntent(context = it, args)
            .also(activityLauncher::launch)
    }
}
