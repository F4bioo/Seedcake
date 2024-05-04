package com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isInvisible
import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.extension.isNotNull
import com.fappslab.seedcake.libraries.extension.isNull

internal const val PIN_PATTERN = "^[0-9]{4}$"
private const val PIN_LENGTH = 4
private const val MIN_PIN_DIGIT = 0
private const val MAX_PIN_DIGIT = 9

class PlutoPinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : LinearLayout(ContextThemeWrapper(context, defStyleAttr), attrs, defStyleAttr) {

    var onWrongPin: (() -> Unit)? = null
    var validation: ((result: PinResult) -> Unit)? = null
    private var buttonPrimary: AppCompatTextView? = null
    private var buttonReset: AppCompatImageView? = null
    private var textError: AppCompatTextView? = null
    private var primaryPin: String? = null
    private var primaryButtonType = PrimaryButtonType.Enter
    private var screenType = ScreenType.Register as ScreenType
    private var currentPin = StringBuilder()
    private val pinValidator = PinValidator()
    private val pinImages = mutableListOf<AppCompatImageView>()
    private val buttonIds = intArrayOf(
        R.id.button_key0, R.id.button_key1, R.id.button_key2, R.id.button_key3, R.id.button_key4,
        R.id.button_key5, R.id.button_key6, R.id.button_key7, R.id.button_key8, R.id.button_key9
    )

    init {
        orientation = VERTICAL
        inflate(context, R.layout.pluto_pin_view, this)

        pinImages.add(findViewById(R.id.image_pin1))
        pinImages.add(findViewById(R.id.image_pin2))
        pinImages.add(findViewById(R.id.image_pin3))
        pinImages.add(findViewById(R.id.image_pin4))

        buttonPrimary = findViewById(R.id.button_primary)
        buttonReset = findViewById(R.id.button_reset)
        textError = findViewById(R.id.text_error)

        buttonIds.forEachIndexed { number, buttonId ->
            findViewById<AppCompatTextView>(buttonId).apply {
                text = number.toString()
                setOnClickListener { appendPin(text.toString()) }
            }
        }

        findViewById<AppCompatImageView>(R.id.button_keyC).apply {
            setOnClickListener { deletePin() }
            setOnLongClickListener {
                if (currentPin.isNotEmpty()) {
                    clearPin(); updatePrimaryButton()
                }
                true
            }
        }

        buttonPrimary?.setOnClickListener {
            onPrimaryButtonClick()
        }

        buttonReset?.setOnClickListener {
            primaryPin = null
            clearPin(); updatePrimaryButton()
            buttonReset?.isInvisible = true
        }

        updatePrimaryButton()
        updateTitle(screenType)
    }

    private fun onPrimaryButtonClick() {
        handlePinValidation { result ->
            when (primaryButtonType) {
                PrimaryButtonType.Next -> {
                    if (result.isValid) {
                        primaryPin = currentPin.toString()
                        buttonReset?.isInvisible = false
                        clearPin(); updatePrimaryButton()
                    }
                }

                PrimaryButtonType.Save -> {
                    if (result.isValid) {
                        validation?.invoke(PinResult.Register(getCurrentPin()))
                    }
                }

                PrimaryButtonType.Unlock -> {
                    if (result.isValid) {
                        validation?.invoke(PinResult.Validate)
                    }
                }

                PrimaryButtonType.Enter -> {
                    // Optional handling if needed
                }
            }
        }
    }

    private fun appendPin(value: String) {
        if (currentPin.length < PIN_LENGTH) {
            currentPin.append(value)
            updatePinImages()
            setErrorMessage(null)
            updatePrimaryButton()

            if (isValidPin() && screenType is ScreenType.Validate) {
                handlePinValidation { result ->
                    if (!result.isValid) {
                        clearPin(); updatePinImages()
                        setErrorMessage(result.errorMessageRes)
                        onWrongPin?.invoke()
                    }
                }
            }
        }
    }

    private fun deletePin() {
        if (currentPin.isNotEmpty()) {
            currentPin.deleteAt(currentPin.lastIndex)
            updatePinImages()
            setErrorMessage(null)
            updatePrimaryButton()
        }
    }

    private fun clearPin() {
        currentPin.clear()
        updatePinImages()
        setErrorMessage(null)
    }

    private fun updatePinImages() {
        pinImages.forEachIndexed { index, imageView ->
            val circleRes = if (index < currentPin.length) {
                R.drawable.circle_close
            } else R.drawable.circle_open
            imageView.setImageResource(circleRes)
        }
    }

    private fun setErrorMessage(@StringRes errorMessageRes: Int?) {
        val errorMessage = errorMessageRes?.let {
            resources.getString(it)
        }.orEmpty()
        textError?.text = errorMessage
    }

    private fun updatePrimaryButton() {
        primaryButtonType = when (screenType) {
            ScreenType.Register -> {
                when {
                    primaryPin.isNotNull() && currentPin.isEmpty() -> PrimaryButtonType.Enter
                    primaryPin.isNull() && isValidPin() -> PrimaryButtonType.Next
                    primaryPin.isNotNull() && isValidPin() -> PrimaryButtonType.Save
                    else -> PrimaryButtonType.Enter
                }
            }

            is ScreenType.Validate -> {
                PrimaryButtonType.Unlock
            }
        }

        buttonPrimary?.setText(primaryButtonType.textRes)
        if (primaryButtonType == PrimaryButtonType.Unlock) {
            buttonReset?.isInvisible = true
        }
    }

    private fun updateTitle(screenType: ScreenType) {
        findViewById<AppCompatTextView>(R.id.text_pin_title).apply {
            val titleRes = when (screenType) {
                ScreenType.Register -> R.string.pin_setup_pin_lock
                is ScreenType.Validate -> R.string.pin_lock
            }
            setText(titleRes)
        }
    }

    fun getCurrentPin(): String {
        return currentPin.toString()
    }

    fun setPinType(type: ScreenType) {
        screenType = type; updatePrimaryButton()
        updateTitle(type)
    }

    fun setupPinPad(shouldShuffle: Boolean) {
        val numbers = getShuffledOrOrderedNumbers(shouldShuffle)
        buttonIds.forEachIndexed { index, buttonId ->
            val button = findViewById<AppCompatTextView>(buttonId)
            val number = numbers[index]
            button.text = number.toString()
        }
    }

    private fun handlePinValidation(validationBock: (ValidationResult) -> Unit) {
        val result = pinValidator.validatePin(getCurrentPin(), primaryPin, screenType)
        setErrorMessage(result.errorMessageRes)
        validationBock(result)
    }

    private fun isValidPin(): Boolean {
        return currentPin.matches(PIN_PATTERN.toRegex())
    }

    private fun getShuffledOrOrderedNumbers(shouldShuffle: Boolean): List<Int> {
        val range = MIN_PIN_DIGIT..MAX_PIN_DIGIT
        return if (shouldShuffle) range.shuffled() else range.toList()
    }
}
