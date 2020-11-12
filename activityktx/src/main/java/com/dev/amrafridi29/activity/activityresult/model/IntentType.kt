package com.dev.amrafridi29.activity.activityresult.model

import android.content.Intent
import java.io.File
import java.io.Serializable

sealed class IntentType : Serializable{
    object Contact : IntentType()
    object OverlayPermission : IntentType()
    data class Other(val intent : Intent) : IntentType()
    object CameraVideo : IntentType()
    data class CameraImage(val authority : String , val storageFileDirectory : File? = null) : IntentType()
    data class KFile(val fileType: FileType, val isMultiSelect : Boolean=false) : IntentType()
    object WriteSettingsPermission : IntentType()
    object AccessUsageSettingsPermission : IntentType()
    object AccessibilitySettingsPermission : IntentType()
    object EnableKeyboard : IntentType()
}