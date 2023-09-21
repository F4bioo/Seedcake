package com.fappslab.features.about.presentation

import android.os.Bundle
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ConcatAdapter
import com.fappslab.features.about.di.AboutModuleLoad
import com.fappslab.features.about.presentation.adapter.header.HeaderAdapter
import com.fappslab.features.about.presentation.adapter.item.ItemAdapter
import com.fappslab.features.about.presentation.extension.getAppVersionCode
import com.fappslab.features.about.presentation.extension.getAppVersionName
import com.fappslab.features.about.presentation.extension.getReleaseDate
import com.fappslab.features.about.presentation.viewmodel.AboutViewAction
import com.fappslab.features.about.presentation.viewmodel.AboutViewModel
import com.fappslab.seedcake.features.about.R
import com.fappslab.seedcake.features.about.databinding.AboutFragmentBinding
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.KoinLazy
import com.fappslab.seedcake.libraries.arch.koin.koinlazy.subModules
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewAction
import com.fappslab.seedcake.libraries.arch.viewmodel.onViewState
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding
import com.fappslab.seedcake.libraries.extension.copyToClipboard
import com.fappslab.seedcake.libraries.extension.gotoPlayStore
import com.fappslab.seedcake.libraries.extension.openInBrowser
import com.fappslab.seedcake.libraries.extension.orDash
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

internal class AboutDialog : DialogFragment(R.layout.about_fragment), KoinLazy {

    private val binding: AboutFragmentBinding by viewBinding()
    private val viewModel: AboutViewModel by viewModel()
    private val adapterHeader by lazy { HeaderAdapter(viewModel::onAdapterItem) }
    private val adapterItem by lazy { ItemAdapter(viewModel::onAdapterItem) }

    override val scope: Scope by fragmentScope()

    override val koinLoad: KoinLoad by subModules(AboutModuleLoad)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        setupListeners()
        setupRecycler()
        setupFooter()
    }

    override fun getTheme(): Int {
        return R.style.PlutoFullScreenDialogAnimStyle
    }

    private fun setupObservables() {
        onViewState(viewModel) { state ->
            adapterHeader.submitList(listOf(state.header))
            adapterItem.submitList(state.items)
        }

        onViewAction(viewModel) { action ->
            when (action) {
                AboutViewAction.CloseClicked -> dismissAllowingStateLoss()
                AboutViewAction.RateClicked -> activity?.gotoPlayStore()
                is AboutViewAction.ItemClicked -> activity?.openInBrowser(action.url)
                is AboutViewAction.FooterClicked ->
                    context?.copyToClipboard(data = action.releaseData)
            }
        }
    }

    private fun setupListeners() = binding.run {
        textRelease.setOnClickListener { viewModel.onFooterClicked(textRelease.text) }
    }

    private fun setupRecycler() = binding.run {
        recyclerAbout.adapter = ConcatAdapter(adapterHeader, adapterItem)
        recyclerAbout.itemAnimator = null
    }

    private fun setupFooter() {
        binding.textRelease.text = getString(
            R.string.about_version_about,
            context?.getAppVersionName().orDash(),
            context?.getAppVersionCode().orDash(),
            context?.getReleaseDate().orDash(),
            context?.packageName.orDash()
        ).parseAsHtml()
    }

    companion object {
        fun createFragment(): AboutDialog =
            AboutDialog()
    }
}
