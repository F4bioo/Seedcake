package com.fappslab.features.encrypt.seed.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fappslab.features.encrypt.main.presentation.REQUEST_KEY_PROGRESS
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress3
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.features.encrypt.seed.di.SeedModuleLoad
import com.fappslab.features.encrypt.seed.presentation.adapter.suggestion.SuggestionAdapter
import com.fappslab.features.encrypt.seed.presentation.component.InputType
import com.fappslab.features.encrypt.seed.presentation.extension.showAlgorithmPage
import com.fappslab.features.encrypt.seed.presentation.extension.showLoadingDialog
import com.fappslab.features.encrypt.seed.presentation.extension.showLockSeedErrorDialog
import com.fappslab.features.encrypt.seed.presentation.viewmodel.seed.SeedViewAction
import com.fappslab.features.encrypt.seed.presentation.viewmodel.seed.SeedViewModel
import com.fappslab.features.encrypt.seed.presentation.viewmodel.seed.SeedViewState
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentSeedBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.extension.safeNavigate
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.emptyString
import com.fappslab.seedcake.libraries.extension.isEnable
import com.fappslab.seedcake.libraries.extension.setFragmentResult
import com.fappslab.seedcake.libraries.extension.setOnCheckListener
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.scope.Scope

internal class SeedFragment : Fragment(R.layout.encrypt_fragment_seed), KoinLazy {

    private val args: SeedFragmentArgs by navArgs()
    private val binding: EncryptFragmentSeedBinding by viewBinding()
    private val viewModel: SeedViewModel by activityViewModel()
    private val adapterSuggestion: SuggestionAdapter by lazy {
        SuggestionAdapter(viewModel::onSuggestionClicked)
    }

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(SeedModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupObservables()
        setupListeners()
    }

    private fun setupObservables() = binding.run {
        onViewState(viewModel) { state ->
            groupTypeState(state)
            inputTypeState(state.inputType)
            progressStepState(state.progress)
            showLoadingDialog(state.shouldShowProgressDialog)
            populateFieldsState(state.inputWords)
            showAlgorithmPageState(state.shouldShowAlgorithmPage)
            textAlgorithmSelectionState(state.algorithmType.cipherSpec.type)
            state.showLockSeedErrorDialogState()
        }

        onViewAction(viewModel) { action ->
            when (action) {
                SeedViewAction.NextInput -> nextInputAction()
                SeedViewAction.BackspaceKey -> backspaceAction()
                is SeedViewAction.LetterKey -> addLetterAction(action.letter)
                is SeedViewAction.Suggestion -> addWordAction(action.suggestion)
                is SeedViewAction.Result -> navigateToDetailsAction(action.args)
            }
        }
    }

    private fun setupListeners() = binding.run {
        includeInputType.groupType.setOnCheckListener { radioIdRes ->
            viewModel.onCheckInputType(radioIdRes)
        }

        buttonEncrypt.setOnClickListener {
            viewModel.onEncrypt(args.secret)
        }

        buttonAlgorithmSelection.setOnClickListener {
            viewModel.onAlgorithmVisibilityPage()
        }

        safeInput.onTextsChanged = { inputWords ->
            viewModel.onWordsChanged(inputWords)
        }

        safeInput.onFieldChanged = {
            viewModel.onNextInput()
        }

        safeInput.onFieldsResized = {
            viewModel.onExpandedFields()
        }

        safeKeyboard.onLetterKeyClicked = { letter ->
            viewModel.onLetterKey(letter)
        }

        safeKeyboard.onBackspaceKeyClicked = {
            viewModel.onBackspaceKey()
        }
    }

    private fun setupRecycler() {
        binding.recyclerSuggestions.adapter = adapterSuggestion
    }

    private fun progressStepState(progress: Int) {
        setFragmentResult(REQUEST_KEY_PROGRESS, pair = Progress3.name to progress)
    }

    private fun SeedViewState.showLockSeedErrorDialogState() {
        showLockSeedErrorDialog(
            dialogErrorPair,
            shouldShowLockSeedErrorDialog,
            viewModel::onLockSeedErrorVisibilityDialog
        )
    }

    private fun showAlgorithmPageState(shouldShowAlgorithmPage: Boolean) {
        showAlgorithmPage(
            shouldShowAlgorithmPage,
            viewModel::onAlgorithmVisibilityPage,
            viewModel::onAlgorithmClicked
        )
    }

    private fun textAlgorithmSelectionState(algorithmType: String) = binding.run {
        textAlgorithmSelection.text = getString(R.string.encrypt_algorithm_selection, algorithmType)
    }

    private fun groupTypeState(state: SeedViewState) = binding.run {
        includeInputType.groupType.isEnable(state.shouldEnableGroupTypes)
        includeInputType.groupType.check(state.inputType.radioIdRes)
    }

    private fun populateFieldsState(inputWords: List<String>) {
        binding.safeInput.populateFields(inputWords)
        afterTextChanged()
    }

    private fun inputTypeState(inputType: InputType) {
        binding.safeInput.setInputType(inputType)
    }

    private fun nextInputAction() {
        adapterSuggestion.submitList(emptyList())
        binding.safeKeyboard.updateKeyAvailability(emptyString())
    }

    private fun addWordAction(suggestion: String) = binding.run {
        safeInput.addWord(suggestion)
        adapterSuggestion.submitList(emptyList())
        safeKeyboard.updateKeyAvailability(suggestion)
    }

    private fun addLetterAction(letter: String) {
        binding.safeInput.addLetter(letter)
        afterTextChanged()
    }

    private fun backspaceAction() {
        binding.safeInput.backspace()
        afterTextChanged()
    }

    private fun afterTextChanged() = binding.run {
        val currentWord = safeInput.currentWord
        val suggestions = safeKeyboard.suggestions(currentWord)
        adapterSuggestion.submitList(suggestions)
        safeKeyboard.updateKeyAvailability(currentWord)
    }

    private fun navigateToDetailsAction(args: ResultArgs) {
        val directions = SeedFragmentDirections.navigateToResultFragment(args)
        findNavController().safeNavigate(directions)
    }
}
