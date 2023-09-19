package com.fappslab.features.decrypt.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fappslab.features.decrypt.presentation.PageFragment
import com.fappslab.features.decrypt.presentation.model.PageArgs
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.seedcake.features.decrypt.R

private const val TOTAL_COUNT = 2

internal class DecryptAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    private val pages = mutableListOf<PageArgs>()

    override fun createFragment(position: Int): Fragment =
        PageFragment.createFragment(pages[position])

    override fun getItemCount(): Int =
        TOTAL_COUNT

    fun submitList(list: List<PageArgs>) {
        pages.addAll(list)
    }
}

internal object PageFactory {
    fun create(pageType: PageType): PageArgs =
        when (pageType) {
            PageType.EncryptedSeed -> PageArgs(
                pageType = pageType,
                hintTextRes = R.string.encrypted_seed,
                placeholderTextRes = R.string.encrypted_seed_example,
                eyeDescriptionTextRes = R.string.decrypted_seed,
                primaryButtonTextRes = R.string.common_decrypt
            )

            PageType.ColoredSeed -> PageArgs(
                pageType = pageType,
                hintTextRes = R.string.colored_seed,
                placeholderTextRes = R.string.colored_seed_example,
                eyeDescriptionTextRes = R.string.uncolored_seed,
                primaryButtonTextRes = R.string.common_decode
            )
        }
}
