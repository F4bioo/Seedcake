package com.fappslab.features.encrypt.seed.presentation.model

import androidx.annotation.StringRes
import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.seedcake.features.encrypt.R

internal enum class AlgorithmType(
    @StringRes val titleRes: Int,
    @StringRes val detailsContentRes: Int,
    val cipherSpec: TransformationType,
    val isRecommended: Boolean = false
) {
    AES_GCM_NOPADDING(
        titleRes = R.string.encrypt_algorithm_gcm_title,
        detailsContentRes = R.string.encrypt_algorithm_gcm_details,
        cipherSpec = TransformationType.GCM,
        isRecommended = true
    ),
    CHACHA20_POLY1305(
        titleRes = R.string.encrypt_algorithm_cha_cha20_title,
        detailsContentRes = R.string.encrypt_algorithm_cha_cha20_details,
        cipherSpec = TransformationType.C20,
    ),
    AES_CTR_NOPADDING(
        titleRes = R.string.encrypt_algorithm_ctr_title,
        detailsContentRes = R.string.encrypt_algorithm_ctr_details,
        cipherSpec = TransformationType.CTR,
    ),
    AES_CBC_PKCS5PADDING(
        titleRes = R.string.encrypt_algorithm_cbc_title,
        detailsContentRes = R.string.encrypt_algorithm_cbc_details,
        cipherSpec = TransformationType.CBC,
    );

    companion object {
        val recommendedType: AlgorithmType
            get() = entries.firstOrNull {
                it.isRecommended
            } ?: AES_GCM_NOPADDING
    }
}
