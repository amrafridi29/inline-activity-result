package com.dev.amrafridi29.activity.permissions.callbacks

import com.dev.amrafridi29.activity.permissions.PermissionResult

interface AcceptedCallback{
    fun onAccepted(result : PermissionResult)
}