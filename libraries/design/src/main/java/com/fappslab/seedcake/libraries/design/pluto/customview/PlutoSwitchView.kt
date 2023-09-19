package com.fappslab.seedcake.libraries.design.pluto.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.withStyledAttributes
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoSwitchView_checked
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoSwitchView_checkedIcon
import com.fappslab.seedcake.libraries.design.R.styleable.PlutoSwitchView_uncheckedIcon

class PlutoSwitchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : FrameLayout(ContextThemeWrapper(context, defStyleAttr), attrs, defStyleAttr) {

    private var isChecked = false
    private var checkedIcon: Int = R.drawable.plu_ic_fullscreen
    private var uncheckedIcon: Int = R.drawable.plu_ic_fullscreen_exit
    private var eyeButton = AppCompatImageButton(
        ContextThemeWrapper(context, R.style.PlutoImageIconButton), attrs, defStyleAttr
    )

    init {
        parseAttributes(attrs)
        addView(eyeButton)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        context?.withStyledAttributes(attrs, R.styleable.PlutoSwitchView) {
            setCheckedIcon(getResourceId(PlutoSwitchView_checkedIcon, checkedIcon))
            setUncheckedIcon(getResourceId(PlutoSwitchView_uncheckedIcon, uncheckedIcon))
            setChecked(getBoolean(PlutoSwitchView_checked, isChecked))
        }
    }

    fun setChecked(shouldChecked: Boolean) {
        isChecked = shouldChecked
        eyeButton.setIcon()
    }

    fun setCheckedIcon(@DrawableRes iconRes: Int) {
        checkedIcon = iconRes
    }

    fun setUncheckedIcon(@DrawableRes iconRes: Int) {
        uncheckedIcon = iconRes
    }

    fun setOnCheckedChangeListener(checkedBlock: (Boolean) -> Unit) {
        eyeButton.setOnClickListener {
            isChecked = isChecked.not()
            setChecked(isChecked)
            checkedBlock(isChecked)
        }
    }

    fun isChecked(): Boolean {
        return isChecked
    }

    private fun AppCompatImageButton.setIcon() {
        val icon = if (isChecked) {
            checkedIcon
        } else uncheckedIcon
        setImageResource(icon)
    }
}
