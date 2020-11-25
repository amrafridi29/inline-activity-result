package com.dev.amrafridi29.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dev.amrafridi29.activity.activityresult.KotlinRuntimeFragment
import com.dev.amrafridi29.activity.activityresult.RuntimeFragment
import com.dev.amrafridi29.activity.activityresult.callbacks.OnCancelCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnPermissionDeniedCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnResultCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnResultResponseCallback
import com.dev.amrafridi29.activity.activityresult.model.FileType
import com.dev.amrafridi29.activity.activityresult.model.IntentType
import com.dev.amrafridi29.activity.activityresult.model.ResultData
import com.dev.amrafridi29.activity.permissions.KotlinRuntimePermission
import com.dev.amrafridi29.activity.permissions.PermissionResult
import com.dev.amrafridi29.activity.permissions.RuntimePermission
import com.dev.amrafridi29.activity.permissions.callbacks.AcceptedCallback
import com.dev.amrafridi29.activity.permissions.callbacks.ResponseCallback
import com.dev.amrafridi29.activity.utils.RealPathUtil.getRealPath
import java.io.File


fun FragmentActivity.askPermission(vararg  permissions: String, acceptedCallback: (PermissionResult) -> Unit) : KotlinRuntimePermission {
    return KotlinRuntimePermission(
        RuntimePermission.askPermission(this)
        .request(*permissions)
        .onAccepted(acceptedCallback))
}

fun Fragment.askPermission(vararg  permissions: String , acceptedCallback: (PermissionResult) -> Unit) : KotlinRuntimePermission {
    return KotlinRuntimePermission(
        RuntimePermission.askPermission(this)
        .request(*permissions)
        .onAccepted(acceptedCallback)
    )
}




fun FragmentActivity.askCamera( authority : String ,storageFileDirectory : File? = null,onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.CameraImage(authority, storageFileDirectory))
            .onResult(onResult))
}

fun FragmentActivity.askContact(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.Contact)
            .onResult(onResult))
}

fun FragmentActivity.askVideoCamera(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.CameraVideo)
            .onResult(onResult))
}

fun FragmentActivity.askForResult(intent: Intent, onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.Other(intent))
            .onResult(onResult))
}

fun FragmentActivity.askOverlayPermission(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.OverlayPermission)
        .onResult(onResult))
}

fun FragmentActivity.askWriteSettingPermission(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.WriteSettingsPermission)
        .onResult(onResult))
}

fun FragmentActivity.askAccessUsageSettingsPermission(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.AccessUsageSettingsPermission)
            .onResult(onResult))
}

fun FragmentActivity.askAccessibilitySettingsPermission(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.AccessibilitySettingsPermission)
            .onResult(onResult))
}

fun FragmentActivity.askEnableKeyboard(onResult: (ResultData) -> Unit): KotlinRuntimeFragment {
    return  KotlinRuntimeFragment(
        RuntimeFragment.askResult(this, IntentType.EnableKeyboard)
            .onResult(onResult))
}

fun FragmentActivity.askFile(fileType : FileType, isMultiSelect :Boolean= false, onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.KFile(fileType, isMultiSelect))
        .onResult(onResult))

fun Fragment.askFile(fileType : FileType, isMultiSelect :Boolean= false, onResult: (ResultData) -> Unit)
        = KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.KFile(fileType, isMultiSelect))
        .onResult(onResult))

fun Fragment.askCamera( authority : String ,storageFileDirectory : File? = null,onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.CameraImage(authority, storageFileDirectory))
        .onResult(onResult))

fun Fragment.askContact(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.Contact)
        .onResult(onResult))

fun Fragment.askVideoCamera(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.CameraVideo)
        .onResult(onResult))

fun Fragment.askForResult(intent: Intent, onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.Other(intent))
        .onResult(onResult))

fun Fragment.askOverlayPermission(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.OverlayPermission)
        .onResult(onResult))

fun Fragment.askWriteSettingPermission(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.WriteSettingsPermission)
        .onResult(onResult))

fun Fragment.askAccessUsageSettingsPermission(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.AccessUsageSettingsPermission)
        .onResult(onResult))

fun Fragment.askAccessibilitySettingsPermission(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.AccessibilitySettingsPermission)
        .onResult(onResult))

fun Fragment.askEnableKeyboard(onResult: (ResultData) -> Unit)
= KotlinRuntimeFragment(
    RuntimeFragment.askResult(this, IntentType.EnableKeyboard)
        .onResult(onResult))

fun Uri.getRealPath(context: Context?) : String?{
    context ?: return null
    return getRealPath(context ,this)
}

fun Uri.getFile(context: Context?) : File?{
    context ?: return null
    val path = getRealPath(context ) ?: return null
    return File(path)
}

fun RuntimeFragment.onResult(block:(ResultData) -> Unit) : RuntimeFragment {
    onResult(object: OnResultCallback {
        override fun onResult(resultData: ResultData) {
            block(resultData)
        }
    })
    return this
}

fun RuntimeFragment.onCancel(block:(ResultData) -> Unit) : RuntimeFragment {
    onCancel(object: OnCancelCallback {
        override fun onCancel(resultData: ResultData) {
            block(resultData)
        }
    })
    return this
}

fun RuntimeFragment.onResponse(block:(ResultData) -> Unit) : RuntimeFragment {
    onResponse(object: OnResultResponseCallback {
        override fun onResultResponse(resultData: ResultData) {
            block(resultData)
        }
    })
    return this
}

fun RuntimeFragment.onPermissionDenied(block:(ResultData) -> Unit) : RuntimeFragment {
    onPermissionDenied(object: OnPermissionDeniedCallback {
        override fun onPermissionDenied(resultData: ResultData) {
            block(resultData)
        }
    })
    return this
}

fun RuntimePermission.onAccepted(block : (PermissionResult) -> Unit) : RuntimePermission {
    onAccepted(object: AcceptedCallback {
        override fun onAccepted(result: PermissionResult) {
            block(result)
        }
    })
    return this
}

fun RuntimePermission.onResponse(block : (PermissionResult) -> Unit) : RuntimePermission {
    onResponse(object: ResponseCallback {
        override fun onResponse(result: PermissionResult) {
            block(result)
        }
    })
    return this
}