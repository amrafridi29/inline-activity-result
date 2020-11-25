package com.dev.amrafridi29.activity.exception

import android.app.Activity
import com.dev.amrafridi29.activity.activityresult.model.ResultData

class RuntimeFragmentException (private val resultData: ResultData) : Exception(){


    fun getForeverDenied()= resultData.getForeverDenied()
    fun getAccepted()= resultData.getAccepted()
    fun getDenied() = resultData.getDenied()
    fun isAccepted()= resultData.isAccepted()
    fun isCanceled() = resultData.resultCode==Activity.RESULT_CANCELED && !hasDenied() && !hasForeverDenied()
    fun askAgain()= resultData.runtimeFragment.ask()
    fun hasDenied() = resultData.hasDenied()
    fun hasForeverDenied() = resultData.hasForeverDenied()
    fun goToSettings() = resultData.goToSettings()
    fun askAgainPermission() = resultData.askAgain()
}