package com.dev.amrafridi29.activity.activityresult.callbacks

import com.dev.amrafridi29.activity.activityresult.model.ResultData


interface OnPermissionDeniedCallback {
    fun onPermissionDenied(resultData: ResultData)
}