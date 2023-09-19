package com.fappslab.features.decrypt.presentation.adapter

import androidx.viewpager2.widget.ViewPager2

internal class PagerChangeCallback(
    val currentPosition: (Int) -> Unit = {}
) : ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        currentPosition(position)
    }
}
