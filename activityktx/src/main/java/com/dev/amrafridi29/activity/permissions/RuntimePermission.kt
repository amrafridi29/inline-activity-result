package com.dev.amrafridi29.activity.permissions

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dev.amrafridi29.activity.ktx.permissions.callbacks.*
import com.dev.amrafridi29.activity.permissions.callbacks.*
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*

class RuntimePermission {
    private var activityReference : Reference<Activity>
    private val permissionsToRequest = mutableListOf<String>()

    //region callbacks
    private val responseCallbacks = mutableListOf<ResponseCallback>()
    private val acceptedCallbacks= mutableListOf<AcceptedCallback>()
    private val foreverDeniedCallbacks = mutableListOf<ForeverDeniedCallback>()
    private val deniedCallbacks = mutableListOf<DeniedCallback>()
    private val permissionListeners = mutableListOf<PermissionListener>()

    companion object{

        fun askPermission(activity: Activity?, vararg permissions: String) : RuntimePermission {
            return RuntimePermission(activity).request(*permissions)
        }

        fun askPermission(fragment: Fragment?, vararg permissions: String) : RuntimePermission {

            return askPermission(fragment?.activity).request(*permissions)
        }
    }

    fun goToSettings() {
        val activity= this.activityReference.get()
        activity?.let {
            val intent= Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", it.packageName, null)
            )
            it.startActivity(intent)
        }
    }



    fun request(vararg permissions: String) : RuntimePermission {
        return this.request(permissions.toList())
    }

    fun request(permissions: List<String>?) : RuntimePermission {
        if(permissions!=null){
            permissionsToRequest.clear()
            permissionsToRequest.addAll(permissions)
        }
        return this
    }

    private val listener = object : PermissionFragment.KPermissionListener {
        override fun onRequestPermissionsResult(
            acceptedPermissions: List<String>,
            refusedPermissions: List<String>,
            askAgainPermissions: List<String>
        ) {
            onReceivedPermissionResult(acceptedPermissions, refusedPermissions, askAgainPermissions)
        }

    }

    private fun onReceivedPermissionResult(
        acceptedPermissions: List<String>,
        refusedPermissions: List<String>,
        askAgainPermissions: List<String>
    ) {
        val permissionResult = PermissionResult(
            this,
            acceptedPermissions,
            refusedPermissions,
            askAgainPermissions
        )
        if(permissionResult.isAccepted()){
            acceptedCallbacks.forEach { c->
                c.onAccepted(permissionResult)
            }
            permissionListeners.forEach { l->
                l.onAccepted(permissionResult, permissionResult.getAccepted())
            }
        }
        if(permissionResult.hasDenied()){
            deniedCallbacks.forEach { c ->
                c.onDenied(permissionResult)
            }
        }

        if(permissionResult.hasForeverDenied()){
            foreverDeniedCallbacks.forEach { c->
                c.onForeverDenied(permissionResult)
            }
        }

        if(permissionResult.hasForeverDenied() || permissionResult.hasDenied()){
            permissionListeners.forEach { l->
                l.onDenied(
                    permissionResult,
                    permissionResult.getDenied(),
                    permissionResult.getForeverDenied()
                )
            }
        }

        responseCallbacks.forEach { c->
            c.onResponse(permissionResult)
        }
    }

    constructor(activity: Activity?){
        if(activity!=null){
            this.activityReference = WeakReference(activity)
        }
        else{
            this.activityReference = WeakReference(null)
        }
    }

    fun onResponse(callback: ResponseCallback) : RuntimePermission {
        responseCallbacks.add(callback)
        return this
    }

    fun onResponse(permissionListener: PermissionListener) : RuntimePermission {
        permissionListeners.add(permissionListener)
        return this
    }

    fun onAccepted(callback: AcceptedCallback): RuntimePermission {
        acceptedCallbacks.add(callback)
        return this
    }

    fun onDenied(callback: DeniedCallback) : RuntimePermission {
        deniedCallbacks.add(callback)
        return this
    }

    fun onForeverDenied(callback: ForeverDeniedCallback) : RuntimePermission {
        foreverDeniedCallbacks.add(callback)
        return this
    }

    fun ask(responseCallback: ResponseCallback){
        onResponse(responseCallback).ask()
    }

    fun ask(permissionListener: PermissionListener){
        onResponse(permissionListener).ask()
    }

    fun findNeededPermission(context: Context) :List<String>{
        return if(permissionsToRequest.isEmpty()){
            findNeededPermissionsFromManifest(context)
        }else
            permissionsToRequest
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun findNeededPermissionsFromManifest(context: Context): List<String> {
        val pm = context.packageManager
        var info: PackageInfo? = null
        try {
            info = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
        } catch (e: PackageManager.NameNotFoundException) { /* */
        }
        val needed: MutableList<String> = ArrayList()
        if (info?.requestedPermissions != null && info.requestedPermissionsFlags != null) {
            for (i in info.requestedPermissions.indices) {
                val flags = info.requestedPermissionsFlags[i]
                var group: String? = null
                try {
                    group = pm.getPermissionInfo(info.requestedPermissions[i], 0).group
                } catch (e: PackageManager.NameNotFoundException) { /* */
                }
                if (flags and PackageInfo.REQUESTED_PERMISSION_GRANTED == 0 && group != null) {
                    needed.add(info.requestedPermissions[i])
                }
            }
        }
        return needed
    }

    fun ask(){
        val mActivity = activityReference.get()
        if(mActivity==null || mActivity.isFinishing)
            return

        val permissions = findNeededPermission(mActivity)

        //No need to ask for permission on Api levels below android marshmallow
        if(permissions.isEmpty() || Build.VERSION.SDK_INT< Build.VERSION_CODES.M || arePermissionsAlreadyAccepted(mActivity,permissions)){
            onAllAccepted(permissions)
        }else{
            val oldFragment =(mActivity as AppCompatActivity).supportFragmentManager
                .findFragmentByTag(PermissionFragment.TAG) as? PermissionFragment
            oldFragment?.setListener(listener) ?: kotlin.run {
                val fragment = PermissionFragment.newInstance(permissions)
                fragment.setListener(listener)
                mActivity.runOnUiThread {
                    mActivity.supportFragmentManager
                        .beginTransaction()
                        .add(fragment , PermissionFragment.TAG)
                        .commitAllowingStateLoss()
                }
            }
        }
    }

    private fun onAllAccepted(permissions: List<String>) {
        onReceivedPermissionResult(permissions, listOf() , listOf())
    }

    private fun arePermissionsAlreadyAccepted(context: Context , permissions: List<String>):Boolean{
        permissions.forEach {
            val permissionState = ContextCompat.checkSelfPermission(context , it)
            if(permissionState==PackageManager.PERMISSION_DENIED)
                return false
        }
        return true
    }

}