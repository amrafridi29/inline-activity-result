package com.dev.amrafridi29.activity.activityresult

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.amrafridi29.activity.activityresult.callbacks.OnCancelCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnPermissionDeniedCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnResultCallback
import com.dev.amrafridi29.activity.activityresult.callbacks.OnResultResponseCallback
import com.dev.amrafridi29.activity.activityresult.model.IntentType
import com.dev.amrafridi29.activity.activityresult.model.PermissionStatus
import com.dev.amrafridi29.activity.activityresult.model.ResultData
import com.dev.amrafridi29.activity.permissions.PermissionResult
import com.dev.amrafridi29.activity.utils.Utils
import java.lang.ref.Reference
import java.lang.ref.WeakReference


class RuntimeFragment {
    private var activityReference: Reference<Activity>
    private var intentType : IntentType?= null

    private val resultResponseCallbacks = mutableListOf<OnResultResponseCallback>()
    private val cancelCallbacks = mutableListOf<OnCancelCallback>()
    private val resultCallbacks = mutableListOf<OnResultCallback>()
    private val permissionDeniedCallbacks = mutableListOf<OnPermissionDeniedCallback>()

    fun getActivity() =activityReference.get()

    companion object{

        @JvmStatic
        fun askResult(activity: Activity?, intentType: IntentType?) : RuntimeFragment {
             return RuntimeFragment(activity, intentType)
        }

        @JvmStatic
        fun askResult(fragment: Fragment?, intentType: IntentType?) : RuntimeFragment {
             return RuntimeFragment(fragment?.activity, intentType)
        }
    }

    private val listener =object : ResultFragment.RuntimeFragmentListener {
        override fun onResponse(
            resultCode: Int,
            intent: Intent?,
            permissionResult: PermissionResult?
        ) {
            if(resultCode==Activity.RESULT_OK && permissionResult==null){
                resultCallbacks.forEach { c->
                    c.onResult(ResultData(resultCode, intent, this@RuntimeFragment, null))
                }
            }

            if(resultCode==Activity.RESULT_CANCELED && permissionResult==null){
                cancelCallbacks.forEach { c->
                    c.onCancel(ResultData(resultCode, intent, this@RuntimeFragment, null))
                }
            }

            if(permissionResult!=null){
                permissionDeniedCallbacks.forEach { c->
                    c.onPermissionDenied(
                        ResultData(
                            resultCode,
                            intent,
                            this@RuntimeFragment,
                            permissionResult
                        )
                    )
                }
            }

            resultResponseCallbacks.forEach { c ->
                c.onResultResponse(ResultData(
                    resultCode,
                    intent,
                    this@RuntimeFragment,
                    permissionResult
                ))
            }
        }

    }



    constructor(activity: Activity?, intentType: IntentType?){
        this.intentType = intentType
        if(activity!=null)
            this.activityReference = WeakReference(activity)
        else this.activityReference= WeakReference(null)
    }

    fun onResponse(callback: OnResultResponseCallback):RuntimeFragment{
        resultResponseCallbacks.add(callback)
        return this
    }

    fun onCancel(callback: OnCancelCallback) : RuntimeFragment {
        cancelCallbacks.add(callback)
        return this
    }

    fun onResult(callback: OnResultCallback): RuntimeFragment {
        resultCallbacks.add(callback)
        return this
    }

    fun onPermissionDenied(callback: OnPermissionDeniedCallback): RuntimeFragment {
        permissionDeniedCallbacks.add(callback)
        return this
    }

    private fun onResult() {
        resultCallbacks.forEach { c ->
            c.onResult(ResultData(Activity.RESULT_OK, null, this, null))
        }
    }

    fun ask(){
        val activity = activityReference.get()
        if(activity== null || activity.isFinishing)
            return

        when(intentType) {
            is IntentType.OverlayPermission -> {
                if (VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(
                        activity
                    )
                )
                    onResult()
                else addFragment(activity)
            }
            is IntentType.WriteSettingsPermission -> {
                if (VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(
                        activity
                    )
                )
                    onResult()
                else addFragment(activity)
            }

            is IntentType.AccessUsageSettingsPermission-> {
                val status = Utils.getUsageStatsPermissionsStatus(activity)
                if (VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    cancelCallbacks.forEach { c->
                        c.onCancel(ResultData(Activity.RESULT_CANCELED, null, this, null))
                    }
                }else if(status== PermissionStatus.GRANTED){
                    onResult()
                }else addFragment(activity)

            }
            else -> {
                addFragment(activity)
            }
        }



    }




    private fun addFragment(activity: Activity){
        val oldFragment = (activity as AppCompatActivity).supportFragmentManager
            .findFragmentByTag(ResultFragment.K_TAG) as? ResultFragment

        oldFragment?.setListener(listener)?: kotlin.run {
            val fragment= ResultFragment.newInstance(intentType)
            fragment.setListener(listener)
            activity.runOnUiThread {
                activity.supportFragmentManager
                    .beginTransaction()
                    .add(fragment, ResultFragment.K_TAG)
                    .commitAllowingStateLoss()
            }
        }
    }
}


