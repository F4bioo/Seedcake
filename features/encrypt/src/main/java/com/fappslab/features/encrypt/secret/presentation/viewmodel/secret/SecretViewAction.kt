package com.fappslab.features.encrypt.secret.presentation.viewmodel.secret

import com.fappslab.features.encrypt.secret.presentation.model.SecretArgs
import com.fappslab.libraries.security.model.SecureString

internal sealed class SecretViewAction {
    data object ShowModal : SecretViewAction()
    data class Use(val secure: SecureString) : SecretViewAction()
    data class Result(val args: SecretArgs) : SecretViewAction()
}
