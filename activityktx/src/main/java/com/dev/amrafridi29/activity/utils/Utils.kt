package com.dev.amrafridi29.activity.utils

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.dev.amrafridi29.activity.activityresult.model.PermissionStatus

object Utils {
     fun getUsageStatsPermissionsStatus(context: Context): PermissionStatus {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return PermissionStatus.CANNOT_BE_GRANTED
        val mode = (context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager)
            .checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.getPackageName()
            )
        val granted = if (mode == AppOpsManager.MODE_DEFAULT) context.checkCallingOrSelfPermission(
            Manifest.permission.PACKAGE_USAGE_STATS
        ) == PackageManager.PERMISSION_GRANTED else mode == AppOpsManager.MODE_ALLOWED
        return if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
    }
}