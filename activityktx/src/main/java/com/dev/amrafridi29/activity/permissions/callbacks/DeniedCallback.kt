package com.dev.amrafridi29.activity.permissions.callbacks

import com.dev.amrafridi29.activity.permissions.PermissionResult

interface DeniedCallback {
    fun onDenied(result : PermissionResult)
}