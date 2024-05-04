package com.fappslab.seedcake.libraries.design.pluto.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity.CENTER_VERTICAL
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoStepProgressView
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoStepProgressView_iconList
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoStepProgressView_primaryColor
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoStepProgressView_progress
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoStepProgressView_secondaryColor
import com.fappslab.seedcake.libraries.extension.dp
import com.google.android.material.progressindicator.LinearProgressIndicator

private const val PROGRESS = 100

class PlutoStepProgressView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageViews = mutableListOf<AppCompatImageView>()
    private val progressBars = mutableListOf<LinearProgressIndicator>()

    @ColorInt
    private var primaryColor: Int = 0

    @ColorInt
    private var secondaryColor: Int = 0

    init {
        initUI()
        parseAttributes()
    }

    private fun initUI() {
        gravity = CENTER_VERTICAL
    }

    private fun parseAttributes() {
        context.withStyledAttributes(attrs, PlutoStepProgressView) {
            primaryColor = getColor(
                PlutoStepProgressView_primaryColor,
                context.getThemeColor(R.attr.colorSecondary)
            )

            secondaryColor = getColor(
                PlutoStepProgressView_secondaryColor,
                ContextCompat.getColor(context, R.color.plu_step_progress_secondary)
            )

            val iconArrayResourceId = getResourceId(PlutoStepProgressView_iconList, 0)
            if (iconArrayResourceId != 0) {
                val iconArray = resources.obtainTypedArray(iconArrayResourceId)
                val iconList = (0 until iconArray.length()).map {
                    iconArray.getResourceId(it, 0)
                }
                iconArray.recycle()
                createSteps(iconList)
            }

            val progress = getInt(PlutoStepProgressView_progress, 0)
            updateProgress(progress)
        }
    }

    fun setPrimaryColor(@ColorInt color: Int) {
        primaryColor = color
        updateViewsColor()
    }

    fun setSecondaryColor(@ColorInt color: Int) {
        secondaryColor = color
        updateViewsColor()
    }

    fun createSteps(icons: List<Int>) {
        icons.forEachIndexed { index, iconRes ->
            val imageView = createStepImage(iconRes)
            addView(imageView)
            imageViews.add(imageView)
            if (index != icons.size.dec()) {
                val progressBar = createStepProgress()
                addView(progressBar)
                progressBars.add(progressBar)
            }
        }
    }

    fun updateProgress(progress: Int) {
        val validProgress = progress.coerceAtMost(imageViews.size)
        imageViews.forEachIndexed { index, imageStep ->
            imageStep.imageTintList = ColorStateList.valueOf(
                if (index <= validProgress) {
                    primaryColor
                } else secondaryColor
            )
        }
        progressBars.forEachIndexed { index, progressStep ->
            val paramsPair = if (index < validProgress) {
                primaryColor to PROGRESS
            } else secondaryColor to 0
            val (color, step) = paramsPair
            progressStep.setIndicatorColor(color)
            progressStep.setProgressCompat(step, true)
        }
    }

    private fun createStepImage(@DrawableRes iconRes: Int): AppCompatImageView {
        return AppCompatImageView(context).apply {
            val size = 30.dp
            layoutParams = LayoutParams(size, size)
            setImageResource(iconRes)
        }
    }

    private fun createStepProgress(): LinearProgressIndicator {
        return LinearProgressIndicator(context).apply {
            layoutParams = LayoutParams(0, WRAP_CONTENT, 1f)
            setProgressCompat(0, false)
            setIndicatorColor(primaryColor)
            trackColor = secondaryColor
            isIndeterminate = false
        }
    }

    private fun updateViewsColor() {
        imageViews.forEach { imageStep ->
            imageStep.imageTintList = ColorStateList.valueOf(primaryColor)
        }
        progressBars.forEach { progressStep ->
            progressStep.setIndicatorColor(primaryColor)
            progressStep.trackColor = secondaryColor
        }
    }

    private fun Context.getThemeColor(@AttrRes attr: Int): Int {
        return TypedValue().let { outValue ->
            theme.resolveAttribute(attr, outValue, true)
            outValue.data
        }
    }
}

