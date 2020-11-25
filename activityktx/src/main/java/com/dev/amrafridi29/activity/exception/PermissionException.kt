package com.dev.amrafridi29.activity.exception

import com.dev.amrafridi29.activity.permissions.PermissionResult
import com.dev.amrafridi29.activity.permissions.RuntimePermission
import java.lang.Exception

class PermissionException ( var permissionResult: PermissionResult) : Exception(){
    val accepted: List<String>
    val foreverDenied: List<String>
    val denied: List<String>
    val runtimePermission: RuntimePermission

    init {
        accepted = permissionResult.getAccepted()
        foreverDenied = permissionResult.getForeverDenied()
        denied = permissionResult.getDenied()
        runtimePermission = permissionResult.getKRuntimePermission()
    }

    fun goToSettings(){
        permissionResult.goToSettings()
    }

    fun askAgain(){
        permissionResult.askAgain()
    }

    fun isAccepted(): Boolean {
        return permissionResult.isAccepted()
    }

    fun hasDenied(): Boolean {
        return permissionResult.hasDenied()
    }

    fun hasForeverDenied(): Boolean {
        return permissionResult.hasForeverDenied()
    }
}