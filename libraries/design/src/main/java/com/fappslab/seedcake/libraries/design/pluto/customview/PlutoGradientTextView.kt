package com.fappslab.seedcake.libraries.design.pluto.customview

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import com.fappslab.seedcake.libraries.design.R

class PlutoGradientTextView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var colors: IntArray? = null
    private var directionType: DirectionType? = null

    init {
        parseAttributes()
    }

    private fun parseAttributes() {
        context.withStyledAttributes(attrs, R.styleable.PlutoGradientTextView) {
            runCatching {
                val colorArrayResourceId = getResourceId(
                    R.styleable.PlutoGradientTextView_colorList, 0
                )

                if (colorArrayResourceId != 0) {
                    colors = resources.getIntArray(colorArrayResourceId)
                }

                directionType = if (hasValue(R.styleable.PlutoGradientTextView_gradientDirection)) {
                    val value = getInt(R.styleable.PlutoGradientTextView_gradientDirection, 0)
                    DirectionType.values()[value]
                } else null
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        colors?.let {
            val directions = directions(w)
            val shader = LinearGradient(
                directions[0].toFloat(),
                0f,
                directions[1].toFloat(),
                0f,
                it,
                null,
                Shader.TileMode.CLAMP
            )
            paint.shader = shader
        }
    }

    private fun directions(w: Int): IntArray =
        directionType?.let {
            when (it) {
                DirectionType.START -> intArrayOf(w, 0)
                DirectionType.END -> intArrayOf(0, w)
            }
        } ?: intArrayOf(0, 0)

    enum class DirectionType {
        START,
        END
    }
}
