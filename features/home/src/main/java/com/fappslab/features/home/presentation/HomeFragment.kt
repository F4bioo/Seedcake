package com.fappslab.features.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.features.common.navigation.DetailsNavigation
import com.fappslab.features.common.navigation.EncryptNavigation
import com.fappslab.features.home.di.HomeModuleLoad
import com.fappslab.features.home.presentation.adapter.HomeAdapter
import com.fappslab.features.home.presentation.viewmodel.HomeViewAction
import com.fappslab.features.home.presentation.viewmodel.HomeViewModel
import com.fappslab.seedcake.features.home.R
import com.fappslab.seedcake.features.home.databinding.HomeFragmentBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class HomeFragment : Fragment(R.layout.home_fragment), KoinLazy {

    private val binding: HomeFragmentBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModel()
    private val detailsNavigation: DetailsNavigation by inject()
    private val encryptNavigation: EncryptNavigation by inject()
    private val adapterMain by lazy { HomeAdapter(viewModel::onAdapterItem) }

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(HomeModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupRecycler()
        setupListeners()
    }

    override fun onResume() {
        viewModel.onResume()
        super.onResume()
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            adapterMain.submitList(state.seeds)
            flipperChildState(state.childPosition)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                HomeViewAction.Resume -> resumeAction()
                HomeViewAction.Add -> navigateToEncryptAction()
                is HomeViewAction.AdapterItem -> navigateToDetailsAction(action.args)
            }
        }
    }

    private fun setupRecycler() = binding.run {
        recyclerSeeds.adapter = adapterMain
    }

    private fun setupListeners() = binding.run {
        addButton.setOnClickListener { viewModel.onAddButton() }
    }

    private fun flipperChildState(childPosition: Int) = binding.run {
        flipperContainer.displayedChild = childPosition
    }

    private fun resumeAction() {
        binding.addButton.show()
    }

    private fun navigateToEncryptAction() = context?.let {
        encryptNavigation.createEncryptIntent(context = it)
            .also(::startActivity)
    }

    private fun navigateToDetailsAction(args: DetailsArgs) = context?.let {
        detailsNavigation.createDetailsIntent(context = it, args)
            .also(::startActivity)
    }
}
