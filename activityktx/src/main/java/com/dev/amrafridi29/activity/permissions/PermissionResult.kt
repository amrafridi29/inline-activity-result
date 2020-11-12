package com.dev.amrafridi29.activity.permissions

data class PermissionResult(private val runtimePermission: RuntimePermission,
                            private val accepted : List<String>,
                            private val foreverDenied : List<String>,
                            private val denied : List<String>
){

    fun askAgain() = runtimePermission.ask()
    fun isAccepted() = foreverDenied.isEmpty() && denied.isEmpty()
    fun getKRuntimePermission()= runtimePermission
    fun goToSettings() = runtimePermission.goToSettings()
    fun hasDenied() = denied.isNotEmpty()
    fun hasForeverDenied() = foreverDenied.isNotEmpty()
    fun getAccepted() = accepted
    fun getForeverDenied()= foreverDenied
    fun getDenied() = denied
}