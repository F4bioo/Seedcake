package com.fappslab.features.lock.presentation.viewmodel

import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult

internal sealed class LockViewAction {
    object FinishApp : LockViewAction()
    object FinishView : LockViewAction()
    object ShowBiometric : LockViewAction()
    data class Preferences(val result: PinResult) : LockViewAction()
}
