package com.fappslab.features.about.di

import com.fappslab.features.about.presentation.model.Item
import com.fappslab.features.about.presentation.viewmodel.AboutViewModel
import com.fappslab.seedcake.features.about.R
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object AboutModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            AboutViewModel(items = provideItems())
        }
    }

    private fun provideItems(): List<Item> {
        val gitHubItem = Item(
            iconRes = R.drawable.plu_ic_git_hub,
            textRes = R.string.app_name,
            externalLink = "https://github.com/F4bioo/Seedcake"
        )
        return listOf(
            gitHubItem,
        )
    }
}
