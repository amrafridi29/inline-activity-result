package com.dev.amrafridi29.activity.permissions

import com.dev.amrafridi29.activity.permissions.callbacks.ResponseCallback




class KotlinRuntimePermission(var runtimePermission: RuntimePermission) {
    init {
        runtimePermission.ask()
    }

    fun onDeclined(block: ((PermissionResult) -> Unit)) : KotlinRuntimePermission {
        runtimePermission.onResponse(object : ResponseCallback {
            override fun onResponse(result: PermissionResult) {
                if(result.hasDenied()|| result.hasForeverDenied()){
                    block(result)
                }
            }
        })
        return this
    }

}