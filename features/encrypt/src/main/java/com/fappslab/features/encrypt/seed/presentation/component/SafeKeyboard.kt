package com.fappslab.features.encrypt.seed.presentation.component

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.children
import com.fappslab.features.encrypt.seed.presentation.component.extension.nextPossibleChars
import com.fappslab.libraries.security.bip39words.Bip39WordsImpl
import com.fappslab.seedcake.features.encrypt.R
import com.google.android.material.button.MaterialButton

private const val COMPLETION_THRESHOLD = 1
private const val DELAY_LONG_PRESS = 200L
private const val PRIMARY_ROW = 1
private const val SECONDARY_ROW = 2
private const val ROW_FROM_Q_TO_P = 10
private const val ROW_FROM_A_TO_L = 9
private const val ROW_FROM_Z_TO_M = 7
private const val QWERTY_KEYS = "qwertyuiopasdfghjklzxcvbnm"
internal const val BACKSPACE_KEY = "\u232B"

@Suppress("MagicNumber")
internal class SafeKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Runnable {

    private val backspaceHandler = Handler(Looper.getMainLooper())
    private var keys = emptyList<Char>()
    private var isLongPress = false
    private val keysPerRow = listOf(
        ROW_FROM_Q_TO_P,
        ROW_FROM_A_TO_L,
        ROW_FROM_Z_TO_M
    )

    val wordList by lazy { Bip39WordsImpl(context).wordList() }
    var onLetterKeyClicked: ((String) -> Unit)? = null
    var onBackspaceKeyClicked: (() -> Unit)? = null

    init {
        orientation = VERTICAL
        keys = QWERTY_KEYS.toList()
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        orientation = VERTICAL
        setupViews()
    }

    override fun run() {
        isLongPress = true
        onBackspaceKeyClicked?.invoke()
        backspaceHandler.postDelayed(this, DELAY_LONG_PRESS)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetBackspaceHandler()
    }

    fun updateKeyAvailability(currentSequence: String) {
        val nextPossibleChars = wordList.nextPossibleChars(currentSequence)
        updateKeysEnabledState(nextPossibleChars)
    }

    fun suggestions(text: String): List<String> {
        return if (text.length >= COMPLETION_THRESHOLD) {
            wordList.filter { it.startsWith(prefix = text, ignoreCase = true) }
        } else emptyList()
    }

    private fun setupViews() {
        keysPerRow.forEachIndexed { rowIndex, _ ->
            val keysRow = createKeyboardRow(rowIndex)
            addView(keysRow)
        }
    }

    private fun createKeyboardRow(rowIndex: Int): LinearLayout {
        val keysRow = LinearLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = HORIZONTAL
        }

        keysRow.addStartingSpacerIfNeeded(rowIndex)

        val startKeyIndex = keysPerRow.take(rowIndex).sum()
        repeat(keysPerRow[rowIndex]) { position ->
            val keyChar = keys[startKeyIndex + position]
            keysRow.addView(createLetterButton(keyChar.toString()))
        }
        keysRow.addEndingComponentIfNeeded(rowIndex)

        return keysRow
    }

    private fun LinearLayout.addStartingSpacerIfNeeded(rowIndex: Int) {
        val spacerWeight = when (rowIndex) {
            PRIMARY_ROW -> 0.5f
            SECONDARY_ROW -> 1.5f
            else -> null
        }
        spacerWeight?.let { addView(createSpacer(it)) }
    }

    private fun LinearLayout.addEndingComponentIfNeeded(rowIndex: Int) {
        val view = when (rowIndex) {
            SECONDARY_ROW -> createBackspaceButton()
                .apply { layoutParams = (layoutParams as LayoutParams).also { it.weight = 1.5f } }

            PRIMARY_ROW -> createSpacer(weight = 0.5f)
            else -> null
        }
        view?.let(::addView)
    }

    private fun createBackspaceButton(): MaterialButton {
        return createButtonKey(BACKSPACE_KEY).apply {
            backgroundTintList = context.getColorStateList(R.color.plu_highlight_keys)
            setOnClickListener { }
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isLongPress = false
                        createBackspaceRunnable()
                        true
                    }

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> {
                        resetBackspaceHandler()
                        if (!isLongPress) {
                            v.performClick()
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun createBackspaceRunnable() {
        backspaceHandler.post(this)
    }

    private fun resetBackspaceHandler() {
        backspaceHandler.removeCallbacks(this)
    }

    private fun createLetterButton(key: String): MaterialButton {
        return createButtonKey(key).apply {
            setOnClickListener { onLetterKeyClicked?.invoke(key) }
        }
    }

    private fun createSpacer(weight: Float): Space {
        return Space(context).apply {
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, weight)
        }
    }

    private fun createButtonKey(key: String): MaterialButton {
        val context = ContextThemeWrapper(context, R.style.SafeKeyboard)
        return MaterialButton(context, null, R.attr.materialButtonToggleGroupStyle).apply {
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f)
            text = key
        }
    }

    private fun updateKeysEnabledState(nextPossibleChars: Set<Char>) {
        children.filterIsInstance<LinearLayout>().forEach { row ->
            row.children.filterIsInstance<MaterialButton>().forEach { keyView ->
                keyView.isEnabled = keyView.text.first() in
                        nextPossibleChars || keyView.text == BACKSPACE_KEY
            }
        }
    }
}
