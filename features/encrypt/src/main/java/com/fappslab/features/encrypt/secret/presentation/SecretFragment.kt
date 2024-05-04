package com.fappslab.features.encrypt.secret.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fappslab.features.encrypt.main.presentation.REQUEST_KEY_PROGRESS
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress2
import com.fappslab.features.encrypt.secret.di.SecretModuleLoad
import com.fappslab.features.encrypt.secret.presentation.extension.getFormParams
import com.fappslab.features.encrypt.secret.presentation.model.SecretArgs
import com.fappslab.features.encrypt.secret.presentation.viewmodel.secret.SecretViewAction
import com.fappslab.features.encrypt.secret.presentation.viewmodel.secret.SecretViewModel
import com.fappslab.libraries.security.model.SecureString
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentSecretBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.extension.safeNavigate
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.inputError
import com.fappslab.seedcake.libraries.extension.setFragmentResult
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

internal class SecretFragment : Fragment(R.layout.encrypt_fragment_secret), KoinLazy {

    private val binding: EncryptFragmentSecretBinding by viewBinding()
    private val viewModel: SecretViewModel by viewModel()

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(SecretModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupListeners()
    }

    private fun setupObservables() = binding.run {
        onViewState(viewModel) { state ->
            progressStepState(state.progress)
            inputLayoutPassphrase1.inputError(state.passphrase1ErrorRes)
            inputLayoutPassphrase2.inputError(state.passphrase2ErrorRes)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                SecretViewAction.ShowModal -> showPassphraseGeneratorModalAction()
                is SecretViewAction.Result -> navigateToDetailsAction(action.args)
                is SecretViewAction.Use -> useAction(action.secure)
            }
        }
    }

    private fun setupListeners() = binding.run {
        buttonNext.setOnClickListener {
            viewModel.onNext(getFormParams())
        }

        inputPassphrase1.doAfterTextChanged { passphrase ->
            viewModel.onAfterChangePassphrase1(passphrase.toString())
        }

        inputPassphrase2.doAfterTextChanged { passphrase ->
            viewModel.onAfterChangePassphrase2(passphrase.toString())
        }

        buttonGeneratePassphrase.setOnClickListener {
            viewModel.onShowPassphraseGeneratorModal()
        }
    }

    private fun progressStepState(progress: Int) {
        setFragmentResult(REQUEST_KEY_PROGRESS, pair = Progress2.name to progress)
    }

    private fun showPassphraseGeneratorModalAction() {
        GeneratorBottomSheet.createFragment {
            onUseClicked = viewModel::onUsePassphrase
        }.build(shouldShow = true, childFragmentManager, TAG_GENERATOR_MODAL)
    }

    private fun useAction(securePassphrase: SecureString) = binding.run {
        securePassphrase.use {
            val secureString = String(it)
            inputPassphrase1.setText(secureString)
            inputPassphrase2.setText(secureString)
        }
    }

    private fun navigateToDetailsAction(args: SecretArgs) {
        val directions = SecretFragmentDirections.navigateToSeedFragment(args)
        findNavController().safeNavigate(directions)
    }
}
