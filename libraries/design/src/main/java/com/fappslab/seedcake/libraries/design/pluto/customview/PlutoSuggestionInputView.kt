package com.fappslab.seedcake.libraries.design.pluto.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.content.withStyledAttributes
import androidx.core.widget.doAfterTextChanged
import com.fappslab.seedcake.libraries.design.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val MIN_WORD_LENGTH_FOR_SUGGESTIONS = 3

class PlutoSuggestionInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val editText: TextInputEditText
    private val inputLayout: TextInputLayout
    private val suggestionContainer: LinearLayout

    private var wordList = emptyList<String>()

    // This variable will help us track if a suggestion has been selected
    private var isSuggestionSelected = false

    init {
        orientation = VERTICAL
        val view = LayoutInflater.from(context)
            .inflate(R.layout.suggestion_input_layout, this, true)
        editText = view.findViewById(R.id.edit_text)
        inputLayout = view.findViewById(R.id.input_layout)
        suggestionContainer = view.findViewById(R.id.suggestion_container)

        editText.doAfterTextChanged { s ->
            if (isSuggestionSelected.not() && s?.lastOrNull() != ' ') {
                refreshSuggestions(s.toString())
            } else {
                // If a suggestion was selected or last character is a space,
                // we clear the flag and the existing suggestions
                isSuggestionSelected = false
                clearSuggestions()
            }
        }

        context.withStyledAttributes(attrs, R.styleable.PlutoSuggestionInputView) {
            val wordArrayResourceId =
                getResourceId(R.styleable.PlutoSuggestionInputView_wordList, 0)
            if (wordArrayResourceId != 0) {
                val wordArray = resources.getStringArray(wordArrayResourceId)
                setWordList(wordArray.toList())
            }

            val hint = getString(R.styleable.PlutoSuggestionInputView_setHint)
            setHint(hint)
        }
    }

    fun setWordList(wordList: List<String>) {
        this.wordList = wordList
    }

    fun setHint(hint: String?) {
        inputLayout.hint = hint
    }

    fun setText(text: String) {
        editText.setText(text)
    }

    fun setText(@StringRes textRes: Int) {
        editText.setText(textRes)
    }

    private fun refreshSuggestions(input: String) {
        clearSuggestions()

        // We split the input into words and get the last word
        val lastWord = input.split(" ").last()

        // If the last word is less than 3 characters, we don't show any suggestions
        if (lastWord.length < MIN_WORD_LENGTH_FOR_SUGGESTIONS) {
            return
        }

        val suggestions = wordList.filter { it.startsWith(lastWord, ignoreCase = true) }
        for (suggestion in suggestions) {
            val button = Button(context).apply {
                text = suggestion
                setOnClickListener {
                    isSuggestionSelected = true
                    val text = input.replaceRange(
                        input.lastIndexOf(lastWord),
                        input.length,
                        suggestion
                    ) + " "
                    editText.setText(text)
                    editText.setSelection(editText.text?.length ?: 0)
                }
            }
            suggestionContainer.addView(button)
        }
    }

    private fun clearSuggestions() {
        suggestionContainer.removeAllViews()
    }
}


