package com.dev.amrafridi29.activity.activityresult

import com.dev.amrafridi29.activity.activityresult.callbacks.OnCancelCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnPermissionDeniedCallback
import com.dev.amrafridi29.activity.activityresult.model.ResultData

class KotlinRuntimeFragment(var runtimeFragment : RuntimeFragment) {
    init {
        runtimeFragment.ask()
    }

    fun onCancel(block : ((ResultData) -> Unit)) : KotlinRuntimeFragment {
        runtimeFragment.onCancel(object  : OnCancelCallback {
            override fun onCancel(resultData: ResultData) {
                block(resultData)
            }
        })

        return this
    }

    fun onPermissionDenied(block : ((ResultData) -> Unit)) : KotlinRuntimeFragment {
        runtimeFragment.onPermissionDenied(object  : OnPermissionDeniedCallback {
            override fun onPermissionDenied(resultData: ResultData) {
                block(resultData)
            }
        })

        return this
    }
}