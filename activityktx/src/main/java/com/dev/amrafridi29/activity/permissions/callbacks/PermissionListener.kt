package com.dev.amrafridi29.activity.permissions.callbacks

import com.dev.amrafridi29.activity.permissions.PermissionResult

interface PermissionListener {
    fun onAccepted(permissionResult: PermissionResult?, accepted: List<String?>?)
    fun onDenied(
        permissionResult: PermissionResult?,
        denied: List<String?>?,
        foreverDenied: List<String?>?
    )
}