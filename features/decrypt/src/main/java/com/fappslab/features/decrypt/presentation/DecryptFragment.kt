package com.fappslab.features.decrypt.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fappslab.features.decrypt.presentation.adapter.DecryptAdapter
import com.fappslab.features.decrypt.presentation.adapter.PageFactory
import com.fappslab.features.decrypt.presentation.adapter.PagerChangeCallback
import com.fappslab.features.decrypt.presentation.extension.setTabName
import com.fappslab.features.decrypt.presentation.model.PageArgs
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.seedcake.features.decrypt.R
import com.fappslab.seedcake.features.decrypt.databinding.DecryptFragmentBinding
import com.fappslab.seedcake.libraries.design.viewbinding.viewBinding

internal class DecryptFragment : Fragment(R.layout.decrypt_fragment) {

    private val binding: DecryptFragmentBinding by viewBinding()
    private val decryptAdapter by lazy { DecryptAdapter(childFragmentManager, lifecycle) }
    private val changeCallback by lazy { PagerChangeCallback() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    override fun onDestroyView() {
        binding.pagerContainer
            .unregisterOnPageChangeCallback(changeCallback)
        super.onDestroyView()
    }

    private fun setupAdapter() = binding.run {
        pagerContainer.registerOnPageChangeCallback(changeCallback)
        pagerContainer.adapter = decryptAdapter
        pagerContainer.setTabName(pageTab)
        decryptAdapter.submitList(list = createPages())
    }

    private fun createPages(): List<PageArgs> {
        return listOf(
            PageFactory.create(PageType.EncryptedSeed),
            PageFactory.create(PageType.ColoredSeed)
        )
    }
}
