package com.fappslab.seedcake.libraries.arch.simplepermission.model

sealed class PermissionStatus {
    object Granted : PermissionStatus()
    object Denied : PermissionStatus()
    object AlwaysDenied : PermissionStatus()
}
