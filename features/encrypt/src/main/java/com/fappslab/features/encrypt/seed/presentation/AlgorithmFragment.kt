package com.fappslab.features.encrypt.seed.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.fappslab.features.encrypt.seed.di.SeedModuleLoad
import com.fappslab.features.encrypt.seed.presentation.adapter.algorithm.AlgorithmAdapter
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmType
import com.fappslab.features.encrypt.seed.presentation.viewmodel.algorithm.AlgorithmViewAction
import com.fappslab.features.encrypt.seed.presentation.viewmodel.algorithm.AlgorithmViewModel
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentAlgorithmBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.scope.Scope

internal class AlgorithmFragment : DialogFragment(R.layout.encrypt_fragment_algorithm), KoinLazy {

    private val binding: EncryptFragmentAlgorithmBinding by viewBinding()
    private val viewModel: AlgorithmViewModel by activityViewModel()
    private val algorithmAdapter: AlgorithmAdapter by lazy {
        AlgorithmAdapter(viewModel::onItemClicked)
    }

    var onItemClicked: ((AlgorithmType) -> Unit)? = null
    var onCloseClicked: (() -> Unit)? = null
    var shouldBlock: Boolean = true

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(SeedModuleLoad)

    override fun getTheme(): Int {
        return R.style.PlutoFullScreenDialogAnimStyle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupListeners()
        setupBehavior()
        setupView()
    }

    private fun setupBehavior() {
        if (shouldBlock) isCancelable = false
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            algorithmAdapter.submitList(state.algorithms)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                is AlgorithmViewAction.UseButton -> onItemClicked?.invoke(action.algorithmType)
                AlgorithmViewAction.CloseButton -> onCloseClicked?.invoke()
            }
        }
    }

    private fun setupListeners() {
        binding.buttonClose.setOnClickListener {
            viewModel.onCloseClicked()
        }
    }

    private fun setupView() = binding.run {
        recyclerAlgorithm.adapter = algorithmAdapter
    }

    companion object {
        fun createFragment(
            block: AlgorithmFragment.() -> Unit
        ): AlgorithmFragment = AlgorithmFragment().apply(block)
    }
}
