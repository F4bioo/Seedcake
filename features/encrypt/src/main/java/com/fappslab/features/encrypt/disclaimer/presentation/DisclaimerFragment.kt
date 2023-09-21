package com.fappslab.features.encrypt.disclaimer.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fappslab.features.encrypt.disclaimer.di.DisclaimerModuleLoad
import com.fappslab.features.encrypt.disclaimer.presentation.extension.showDisclaimerErrorDialog
import com.fappslab.features.encrypt.disclaimer.presentation.viewmodel.DisclaimerViewAction
import com.fappslab.features.encrypt.disclaimer.presentation.viewmodel.DisclaimerViewModel
import com.fappslab.features.encrypt.main.presentation.REQUEST_KEY_PROGRESS
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress1
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentDisclaimerBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.extension.navigateWithAnimations
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.setFragmentResult
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

internal class DisclaimerFragment : Fragment(R.layout.encrypt_fragment_disclaimer), KoinLazy {

    private val binding: EncryptFragmentDisclaimerBinding by viewBinding()
    private val viewModel: DisclaimerViewModel by viewModel()

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(DisclaimerModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupListeners()
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            showDisclaimerErrorDialogState(state.shouldShowError)
            checkBoxConfirmState(state.isConfirmChecked)
            progressStepState(state.progress)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                DisclaimerViewAction.Continue ->
                    navigateToEncryptAction()
            }
        }
    }

    private fun setupListeners() = binding.run {
        checkBoxConfirm.setOnCheckedChangeListener { _, isChecked -> viewModel.onConfirm(isChecked) }
        buttonContinue.setOnClickListener { viewModel.onContinue() }
    }

    private fun showDisclaimerErrorDialogState(shouldShowError: Boolean) {
        showDisclaimerErrorDialog(shouldShowError, viewModel::onDismissError)
    }

    private fun progressStepState(progress: Int) {
        setFragmentResult(REQUEST_KEY_PROGRESS, pair = Progress1.name to progress)
    }

    private fun checkBoxConfirmState(isConfirmChecked: Boolean) {
        binding.checkBoxConfirm.isChecked = isConfirmChecked
    }

    private fun navigateToEncryptAction() {
        val directions = DisclaimerFragmentDirections.navigateToEncryptFragment()
        findNavController().navigateWithAnimations(directions)
    }
}
