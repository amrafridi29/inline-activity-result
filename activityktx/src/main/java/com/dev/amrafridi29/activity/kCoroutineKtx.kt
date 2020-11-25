package com.dev.amrafridi29.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dev.amrafridi29.activity.activityresult.RuntimeFragment
import com.dev.amrafridi29.activity.activityresult.model.FileType
import com.dev.amrafridi29.activity.activityresult.model.IntentType
import com.dev.amrafridi29.activity.activityresult.model.ResultData
import com.dev.amrafridi29.activity.exception.NoActivityException
import com.dev.amrafridi29.activity.exception.PermissionException
import com.dev.amrafridi29.activity.exception.RuntimeFragmentException
import com.dev.amrafridi29.activity.permissions.PermissionResult
import com.dev.amrafridi29.activity.permissions.RuntimePermission
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun FragmentActivity.askPermission(vararg permissions : String): PermissionResult = suspendCoroutine { continuation ->
    var resumed  = false
    RuntimePermission.askPermission(this)
        .request(permissions.toList())
        .onResponse { result ->
            if(!resumed){
                resumed=true
                when{
                    result.isAccepted()-> continuation.resume(result)
                    else -> continuation.resumeWithException(PermissionException(result))
                }
            }

        }.ask()
}
suspend fun Fragment.askPermission(vararg permissions : String): PermissionResult = suspendCoroutine { continuation ->
    var resumed  = false
    when(activity){
        null -> continuation.resumeWithException(NoActivityException())
        else-> RuntimePermission.askPermission(this)
            .request(permissions.toList())
            .onResponse { result ->
                if(!resumed){
                    resumed=true
                    when{
                        result.isAccepted()-> continuation.resume(result)
                        else -> continuation.resumeWithException(PermissionException(result))
                    }
                }

            }.ask()
    }
}
suspend fun FragmentActivity.askFile(fileType: FileType, isMultiSelect: Boolean = false) : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.KFile(fileType , isMultiSelect))
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askFile(fileType: FileType, isMultiSelect: Boolean = false) : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null -> continuation.resumeWithException(NoActivityException())
        else -> RuntimeFragment.askResult(this, IntentType.KFile(fileType , isMultiSelect))
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askCamera(authority: String, storageFileDirectory : File? = null) : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.CameraImage(authority , storageFileDirectory))
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askCamera(authority: String, storageFileDirectory : File? = null) : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null-> continuation.resumeWithException(NoActivityException())
        else->RuntimeFragment.askResult(this, IntentType.CameraImage(authority , storageFileDirectory))
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askContact() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.Contact)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askContact() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null -> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.Contact)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askVideoCamera() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.CameraVideo)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askVideoCamera() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null-> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.CameraVideo)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askForResult(intent: Intent) : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.Other(intent))
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askForResult(intent: Intent) : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null-> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.Other(intent))
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askOverlayPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.OverlayPermission)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askOverlayPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null -> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.OverlayPermission)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askWriteSettingPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.WriteSettingsPermission)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askWriteSettingPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null-> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.WriteSettingsPermission)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askAccessUsageSettingPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.AccessUsageSettingsPermission)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askAccessUsageSettingPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null-> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.AccessUsageSettingsPermission)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askAccessibilitySettingPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.AccessibilitySettingsPermission)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askAccessibilitySettingPermission() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null -> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.AccessibilitySettingsPermission)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}
suspend fun FragmentActivity.askEnableKeyboard() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    RuntimeFragment.askResult(this, IntentType.EnableKeyboard)
        .onResponse { resultData ->
            if(!resumed){
                resumed=true
                when (resultData.resultCode) {
                    Activity.RESULT_OK -> continuation.resume(resultData)
                    else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                }
            }
        }.ask()
}
suspend fun Fragment.askEnableKeyboard() : ResultData = suspendCoroutine { continuation ->
    var resumed= false
    when(activity){
        null-> continuation.resumeWithException(NoActivityException())
        else-> RuntimeFragment.askResult(this, IntentType.EnableKeyboard)
            .onResponse { resultData ->
                if(!resumed){
                    resumed=true
                    when (resultData.resultCode) {
                        Activity.RESULT_OK -> continuation.resume(resultData)
                        else -> continuation.resumeWithException(RuntimeFragmentException(resultData))
                    }
                }
            }.ask()
    }
}