package com.fappslab.libraries.security.model

import androidx.annotation.StringRes
import com.fappslab.seedcake.libraries.security.R

enum class ValidationType(@StringRes val messageRes: Int) {
    PASSPHRASE_EMPTY(R.string.passphrase_empty),
    PASSPHRASE_INVALID(R.string.passphrase_invalid),
    READABLE_SEED_PHRASE_EMPTY(R.string.seed_phrase_empty),
    READABLE_SEED_PHRASE_INVALID_LENGTH(R.string.seed_phrase_invalid_length),
    READABLE_SEED_PHRASE_WORD_NOT_IN_LIST(R.string.seed_phrase_word_not_in_list),
    READABLE_SEED_PHRASE_INVALID_FORMAT(R.string.seed_phrase_invalid_format),
    UNREADABLE_SEED_PHRASE_EMPTY(R.string.encryption_phrase_empty),
    DECRYPTION_TIMEOUT(R.string.decryption_timeout),
    DECODE_FAILED(R.string.decode_failed),
    ENCRYPTION_FAILED(R.string.encryption_failed),
    DECRYPTION_FAILED(R.string.decryption_failed),
    BLANK_COLORED_SEED_EXCEPTION(R.string.blank_colored_seed_exception),
    INVALID_COLOR_FORMAT_EXCEPTION(R.string.invalid_color_format_exception),
    INVALID_HEX_COLOR_EXCEPTION(R.string.invalid_hex_color_exception),
    SEQUENTIAL_COLOR_EXCEPTION(R.string.sequential_color_exception),
    VERIFICATION_FAILED(R.string.error_verification_failed),
    INDEX_OUT_OF_BOUNDS_COLOR_EXCEPTION(R.string.index_out_of_bounds_exception)
}
