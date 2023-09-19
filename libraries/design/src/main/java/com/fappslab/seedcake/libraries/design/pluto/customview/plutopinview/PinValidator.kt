package com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview

import com.fappslab.seedcake.libraries.design.R
import com.fappslab.seedcake.libraries.extension.isNotNull

internal class PinValidator {

    fun validatePin(currentPin: String, primaryPin: String?, screenType: ScreenType): ValidationResult {
        return when {
            currentPin.isEmpty() ->
                ValidationResult(
                    isValid = false,
                    errorMessageRes = R.string.pin_cannot_be_empty
                )

            !isValidPin(currentPin) ->
                ValidationResult(
                    isValid = false,
                    errorMessageRes = R.string.pin_must_be_exactly_4_digits
                )

            screenType is ScreenType.Register && primaryPin.isNotNull() && primaryPin != currentPin ->
                ValidationResult(
                    isValid = false,
                    errorMessageRes = R.string.pins_do_not_match
                )

            screenType is ScreenType.Validate && currentPin != screenType.savedPin ->
                ValidationResult(
                    isValid = false,
                    errorMessageRes = R.string.pins_do_not_match
                )

            else -> ValidationResult(
                isValid = true,
                errorMessageRes = null
            )
        }
    }

    private fun isValidPin(pinValue: String): Boolean {
        return pinValue.matches(PIN_PATTERN.toRegex())
    }
}
