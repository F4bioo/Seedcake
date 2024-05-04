package com.fappslab.features.encrypt.secret.presentation.viewmodel.passphrase

import com.fappslab.libraries.security.model.SecureString

internal sealed class GeneratorViewAction {
    data object CloseButton : GeneratorViewAction()
    data object RandomButton : GeneratorViewAction()
    data class Copy(val secure: SecureString) : GeneratorViewAction()
    data class WarningCopy(val secure: SecureString) : GeneratorViewAction()
    data class Generated(val secure: SecureString) : GeneratorViewAction()
    data class UseButton(val secure: SecureString) : GeneratorViewAction()
}
