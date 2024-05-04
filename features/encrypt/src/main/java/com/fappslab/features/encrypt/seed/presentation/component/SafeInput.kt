package com.fappslab.features.encrypt.seed.presentation.component

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.fappslab.features.encrypt.seed.presentation.component.extension.nextPossibleChars
import com.fappslab.libraries.security.bip39words.Bip39WordsImpl
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.extension.clear
import com.fappslab.seedcake.libraries.extension.dp
import com.fappslab.seedcake.libraries.extension.emptyString

private const val COUNT_PER_ROW = 3

@Suppress("MagicNumber", "NestedBlockDepth")
internal class SafeInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var currentInputType = InputType.TWELVE
    private var inputFields = mutableListOf<TextView>()
    private var inputWords = mutableListOf<String>()
    private var currentText = StringBuilder()
    private var currentFieldIndex = 0
    private val isLastKey: Boolean
        get() = wordList.nextPossibleChars(currentWord).isEmpty()

    val wordList by lazy { Bip39WordsImpl(context).wordList() }
    var onFieldChanged: (() -> Unit)? = null
    var onFieldsResized: (() -> Unit)? = null
    var onTextsChanged: ((List<String>) -> Unit)? = null
    val currentWord: String
        get() = currentText.toString()

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply { setPadding(8.dp, 0, 8.dp, 8.dp) }
        orientation = VERTICAL
        setupViews(InputType.TWELVE)
    }

    private fun setupViews(inputType: InputType) {
        removeAllViews()
        currentFieldIndex = 0
        inputFields.clear()
        createInputs(inputType)
        selectFirstInputField()
    }

    fun setInputType(inputType: InputType) {
        if (currentInputType != inputType) {
            currentInputType = inputType
            setupViews(inputType)
        }
    }

    fun addWord(word: String) {
        if (isLastKey && inputWords.size >= InputType.TWENTY_FOUR.count) return

        if (isLastKey && inputFields.size >= InputType.TWELVE.count) {
            onFieldsResized?.invoke()
        }

        if (inputWords.isNotEmpty()) {
            inputWords[inputWords.size.dec()] = word
        } else inputWords.add(word)

        currentText.clear()
        currentText.append(word)

        val currentField = inputFields[currentFieldIndex]
        currentField.text = word

        if (wordList.contains(inputWords.last())) {
            currentFieldIndex++
            if (inputWords.size <= currentFieldIndex) {
                inputWords.add(emptyString())
                onFieldChanged?.invoke()
            }
        }

        onTextsChanged?.invoke(inputWords.toList())
    }

    fun addLetter(letter: String) {
        if (currentFieldIndex >= inputFields.size) return

        val newWord = if (inputWords.isNotEmpty()) {
            inputWords.last() + letter
        } else letter

        if (wordList.any { it.startsWith(newWord) }) {
            if (inputWords.isNotEmpty()) {
                inputWords[inputWords.size.dec()] = newWord
            } else inputWords.add(newWord)

            currentText.clear()
            currentText.append(newWord)

            val currentField = inputFields[currentFieldIndex]
            currentField.text = newWord

            val exclusiveWordMatch = wordList.contains(newWord)
            val exclusivePrefixMatch = !wordList.any { it != newWord && it.startsWith(newWord) }
            if (exclusiveWordMatch && exclusivePrefixMatch) {
                if (currentFieldIndex < inputFields.size) {
                    currentFieldIndex++
                    if (inputWords.size <= currentFieldIndex) {
                        inputWords.add(emptyString())
                        onFieldChanged?.invoke()
                    }
                }
            }

            onTextsChanged?.invoke(inputWords.toList())
        }
    }

    fun backspace() {
        if (inputWords.isEmpty()) return

        val currentField = inputFields[currentFieldIndex]
        val currentWord = currentField.text.toString()

        if (currentWord.isNotEmpty()) {
            val updatedWord = currentWord.dropLast(n = 1)
            inputWords[currentFieldIndex] = updatedWord
            currentField.text = updatedWord
            currentField.isSelected = updatedWord.isNotEmpty()
        } else {
            if (currentFieldIndex > 0) {
                currentField.isSelected = false
                inputWords.removeAt(currentFieldIndex)
                currentFieldIndex--
                inputFields[currentFieldIndex].isSelected = true
            }
        }

        currentText.clear()
        if (inputWords.isNotEmpty()) {
            currentText.append(inputWords.last())
        }

        onTextsChanged?.invoke(inputWords.toList())
    }

    fun populateFields(list: List<String>) {
        clearFields()

        val wordsToUse = list.take(inputFields.size)
        wordsToUse.forEachIndexed { index, word ->
            if (index < inputFields.size) {
                val currentField = inputFields[index]
                currentField.text = word
                currentField.isSelected =
                    index == currentFieldIndex(wordsToUse)
            }
        }

        inputWords.addAll(wordsToUse)

        if (wordsToUse.isNotEmpty()) {
            currentText.append(wordsToUse.last())
        }

        currentFieldIndex = currentFieldIndex(wordsToUse)
    }

    private fun clearFields() {
        inputFields.forEach { field ->
            field.clear()
            field.isSelected = false
        }
        inputWords.clear()
        currentText.clear()
        currentFieldIndex = 0
        selectFirstInputField()
    }

    private fun selectFirstInputField() {
        inputFields.forEachIndexed { index, inputField ->
            inputField.isSelected = index == 0
        }
    }

    private fun currentFieldIndex(list: List<String>): Int {
        return list.size.coerceAtLeast(minimumValue = 1).dec()
    }

    private fun createInputs(inputType: InputType) {
        for (i in 0 until (inputType.count / COUNT_PER_ROW)) {
            val rowLayout = LinearLayout(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 8.dp, 0, 0) }
                orientation = HORIZONTAL
            }
            for (j in 0 until COUNT_PER_ROW) {
                val countNumber = (i * COUNT_PER_ROW + j).inc()
                val numberTextView = createNumberTextView("${countNumber}.")
                val inputTextView = createInputTextView()
                inputFields.add(inputTextView)

                rowLayout.addView(numberTextView)
                rowLayout.addView(inputTextView)
            }
            addView(rowLayout)
        }
    }

    private fun createNumberTextView(countNumber: String): TextView {
        return TextView(context).apply {
            text = countNumber
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 0.2f)
        }
    }

    private fun createInputTextView(): TextView {
        return TextView(context).apply {
            maxLines = 1
            isFocusable = true
            isFocusableInTouchMode = true
            gravity = Gravity.CENTER_VERTICAL
            ellipsize = TextUtils.TruncateAt.END
            setPadding(8.dp, 0, 8.dp, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setBackgroundResource(R.drawable.encrypt_input_field_selector)
            layoutParams = LayoutParams(0, 32.dp, 0.8f)
                .apply { marginEnd = 10.dp }
        }
    }
}
