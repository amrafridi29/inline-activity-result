package com.dev.amrafridi29.activity.permissions.callbacks

import com.dev.amrafridi29.activity.permissions.PermissionResult

interface ForeverDeniedCallback {
    fun onForeverDenied(result : PermissionResult)
}