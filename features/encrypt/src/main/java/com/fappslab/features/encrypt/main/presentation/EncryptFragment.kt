package com.fappslab.features.encrypt.main.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fappslab.features.encrypt.main.di.EncryptModuleLoad
import com.fappslab.features.encrypt.main.presentation.adapter.CachedSeedAdapter
import com.fappslab.features.encrypt.main.presentation.adapter.SpaceTokenizer
import com.fappslab.features.encrypt.main.presentation.extension.getEncryptParams
import com.fappslab.features.encrypt.main.presentation.extension.showLoadingDialog
import com.fappslab.features.encrypt.main.presentation.extension.showPassphraseMismatchErrorDialog
import com.fappslab.features.encrypt.main.presentation.viewmodel.EncryptViewAction
import com.fappslab.features.encrypt.main.presentation.viewmodel.EncryptViewModel
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.libraries.security.bip39words.Bip39Words
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.extension.navigateWithAnimations
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.inputError
import com.fappslab.seedcake.libraries.extension.setFragmentResult
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

private const val DELIMITERS = " "

internal class EncryptFragment : Fragment(R.layout.encrypt_fragment), KoinLazy {

    private val binding: EncryptFragmentBinding by viewBinding()
    private val viewModel: EncryptViewModel by viewModel()
    private val bip39Words: Bip39Words by inject()

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(EncryptModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupListeners()
        setupSuggestion()
    }

    private fun setupObservables() = binding.run {
        onViewState(viewModel) { state ->
            progressStepState(state.progress)
            inputLayoutAlias.inputError(state.aliasErrorRes)
            inputLayoutPassphrase1.inputError(state.passphrase1ErrorRes)
            inputLayoutPassphrase2.inputError(state.passphrase2ErrorRes)
            inputLayoutMnemonic.inputError(state.mnemonicErrorRes)
            showLoadingDialog(state.shouldShowProgressDialog)
            showPassphraseMismatchErrorDialogState(state.shouldShowErrorDialog)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                is EncryptViewAction.Result -> navigateToDetailsAction(action.args)
            }
        }
    }

    private fun setupListeners() = binding.run {
        buttonEncrypt.setOnClickListener {
            inputMnemonic.text.trim().split(DELIMITERS)
                .filter { it.isNotBlank() }
                .also { viewModel.onEncrypt(getEncryptParams(seed = it)) }
        }

        inputAlias.doAfterTextChanged {
            viewModel.onAfterChangeAlias()
        }

        inputPassphrase1.doAfterTextChanged { passphrase ->
            viewModel.onAfterChangePassphrase1(passphrase.toString())
        }

        inputPassphrase2.doAfterTextChanged { passphrase ->
            viewModel.onAfterChangePassphrase2(passphrase.toString())
        }

        inputMnemonic.doAfterTextChanged {
            viewModel.onAfterChangeMnemonic()
        }
    }

    private fun setupSuggestion() {
        lifecycleScope.launch {
            val wordList = bip39Words.wordList()
            binding.inputMnemonic.run {
                setTokenizer(SpaceTokenizer())
                CachedSeedAdapter(context, wordList)
                    .also(::setAdapter)
            }
        }
    }

    private fun progressStepState(progress: Int) {
        setFragmentResult(PROGRESS_KEY, pair = "progress2" to progress)
    }

    private fun showPassphraseMismatchErrorDialogState(shouldShow: Boolean) {
        showPassphraseMismatchErrorDialog(shouldShow, viewModel::onErrorDismiss)
    }

    private fun navigateToDetailsAction(args: ResultArgs) {
        val directions = EncryptFragmentDirections.navigateToResultFragment(args)
        findNavController().navigateWithAnimations(directions)
    }
}
