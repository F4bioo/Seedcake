package com.fappslab.features.lock.presentation.viewmodel

import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult

internal sealed class LockViewAction {
    data object ErrorPin : LockViewAction()
    data object FinishApp : LockViewAction()
    data object FinishView : LockViewAction()
    data object ShowBiometric : LockViewAction()
    data class Preferences(val result: PinResult) : LockViewAction()
}
