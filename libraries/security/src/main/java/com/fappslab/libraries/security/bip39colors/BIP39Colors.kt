package com.fappslab.libraries.security.bip39colors

import com.fappslab.features.common.domain.usecase.DecodeParams
import com.fappslab.features.common.domain.usecase.EncodeParams

interface BIP39Colors {
    suspend fun encodeSeedColor(params: EncodeParams): List<Pair<String, String>>
    suspend fun decodeSeedColor(params: DecodeParams): String
}
