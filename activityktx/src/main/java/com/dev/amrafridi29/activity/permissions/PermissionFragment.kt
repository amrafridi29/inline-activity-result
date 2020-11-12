package com.dev.amrafridi29.activity.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment(){


    private val REQUEST_CODE = 23
    private val permissionsList = mutableListOf<String>()
    private var listener: KPermissionListener?= null

    companion object{
        val LIST_PERMISSIONS = "LIST_PERMISSIONS"
        val TAG = PermissionFragment::class.java.simpleName
        fun newInstance(permissions: List<String>): PermissionFragment {
            val args = Bundle()
            args.putStringArrayList(LIST_PERMISSIONS, ArrayList(permissions))
            val fragment = PermissionFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            val permissionsArgs: List<String>? =
                arguments.getStringArrayList(LIST_PERMISSIONS)
            if (permissionsArgs != null) {
                permissionsList.addAll(permissionsArgs)
            }
        }
    }

     override fun onResume() {
         super.onResume()
         if (permissionsList.size > 0) {
             requestPermissions(permissionsList.toTypedArray(), REQUEST_CODE)
         } else {
             // this shouldn't happen, but just to be sure
             fragmentManager!!.beginTransaction()
                 .remove(this)
                 .commitAllowingStateLoss()
         }
     }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE && permissions.isNotEmpty() && listener!=null) {
            val acceptedPermissions: MutableList<String> = java.util.ArrayList()
            val askAgainPermissions: MutableList<String> = java.util.ArrayList()
            val refusedPermissions: MutableList<String> = java.util.ArrayList()
            for (i in permissions.indices) {
                val permissionName = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    acceptedPermissions.add(permissionName)
                } else {
                    if (shouldShowRequestPermissionRationale(permissionName)) {
                        //listener.onDenied(permissionResult);
                        askAgainPermissions.add(permissionName)
                    } else {
                        refusedPermissions.add(permissionName)
                        //listener.onForeverDenied(permissionResult);
                    }
                }
            }
            Log.e(TAG, "onRequestPermissionsResult: " + acceptedPermissions)
            Log.e(TAG, "onRequestPermissionsResult: " + askAgainPermissions)
            Log.e(TAG, "onRequestPermissionsResult: " + refusedPermissions)
            listener?.onRequestPermissionsResult(
                acceptedPermissions,
                refusedPermissions,
                askAgainPermissions
            )
            fragmentManager!!.beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        }
    }

    fun setListener(listener: KPermissionListener) {
        this.listener= listener
    }

     interface KPermissionListener {
        fun onRequestPermissionsResult(
            acceptedPermissions: List<String>,
            refusedPermissions: List<String>,
            askAgainPermissions: List<String>
        )
    }
}