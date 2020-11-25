package com.dev.amrafridi29.activity.activityresult

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.dev.amrafridi29.activity.activityresult.model.FileType
import com.dev.amrafridi29.activity.activityresult.model.IntentType
import com.dev.amrafridi29.activity.activityresult.model.PermissionStatus
import com.dev.amrafridi29.activity.askPermission
import com.dev.amrafridi29.activity.permissions.PermissionResult
import com.dev.amrafridi29.activity.utils.Utils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


internal class ResultFragment(private val intentType: IntentType?) : Fragment(){

    constructor() :this(null)

    private var cameraFilePath: String = ""

    companion object{
        val K_REQUEST_CODE= 22
        val K_EXTRA_INTENT_TYPE="k_extra_intent_type"
        val K_TAG = ResultFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(intentType: IntentType?) = ResultFragment(intentType)

        @JvmStatic
        fun newInstance() = ResultFragment()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }
    

    private var listener : RuntimeFragmentListener? = null



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ask()
    }
    fun setListener(listener : RuntimeFragmentListener){
        this.listener= listener
    }


    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(cameraFilePath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intent= data ?: Intent()
        when (intentType) {
            is IntentType.CameraImage -> {
                intent.data = Uri.parse(cameraFilePath)
                galleryAddPic()
            }
        }

        when(resultCode){
            Activity.RESULT_CANCELED -> {
                when (intentType) {
                    is IntentType.OverlayPermission -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.canDrawOverlays(requireContext()))
                                onSuccess(Activity.RESULT_OK, intent)
                            else onCancel(resultCode, intent)
                        }
                    }

                    is IntentType.WriteSettingsPermission-> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.System.canWrite(requireContext()))
                                onSuccess(Activity.RESULT_OK, intent)
                            else onCancel(resultCode, intent)
                        }
                    }

                    is IntentType.AccessUsageSettingsPermission-> {
                        val status  = Utils.getUsageStatsPermissionsStatus(requireContext())
                        if(status== PermissionStatus.GRANTED)
                            onSuccess(Activity.RESULT_OK , intent)
                        else
                            onCancel(resultCode ,intent)
                    }

                    else -> onCancel(resultCode, intent)
                }
            }
            Activity.RESULT_OK -> {
                onSuccess(resultCode, intent)
            }
        }
        fragmentManager?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun onSuccess(resultCode: Int, intent: Intent?){
        listener?.onResponse(resultCode, intent , null)
    }

    private fun onCancel(resultCode: Int, intent: Intent?){
        listener?.onResponse(resultCode, intent , null)
    }

    private fun onPermissionResult(permissionResult: PermissionResult){
        listener?.onResponse(Activity.RESULT_CANCELED, null , permissionResult)
        fragmentManager?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
    }

    private fun ask(){
        intentType?.let { intentType->
            when (intentType) {
                is IntentType.Contact -> {
                    Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI).also { intent->
                        intent.resolveActivity(requireActivity().packageManager)?.also {
                            startContactWithPermission(intent)
                        }
                    }

                }
                is IntentType.KFile -> {
                    val intent = when (intentType.fileType) {
                        FileType.AUDIOS -> Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        )
                        FileType.AUDIO_WAVE -> Intent(Intent.ACTION_PICK).apply {
                            type = "audio/x-wav"
                        }
                        FileType.VIDEOS -> Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        )
                        FileType.IMAGES -> Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        FileType.MEDIAS -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                            }
                        }
                        FileType.PDF -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/pdf"
                        }
                        FileType.MSWORD -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/msword"
                        }
                        FileType.POWER_POINT -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/vnd.ms-powerpoint"
                        }
                        FileType.EXCEL -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/vnd.ms-excel"
                        }
                        FileType.TXT -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "text/plain"
                        }

                        FileType.ZIP -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/zip"
                        }

                        FileType.RAR -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/rar"
                        }

                        FileType.ZIP_RAR -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                putExtra(
                                    Intent.EXTRA_MIME_TYPES, arrayOf(
                                        "application/zip",
                                        "application/rar"
                                    )
                                )
                            }
                        }

                        FileType.RTF -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/rtf"
                        }

                        FileType.ANY_FILE -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                        }

                        FileType.APPLICATIONS -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/*"
                        }
                    }

                    intent.also { pickFileIntent ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            pickFileIntent.putExtra(
                                Intent.EXTRA_ALLOW_MULTIPLE,
                                intentType.isMultiSelect
                            )
                        }

                        pickFileIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startIntentWithPermission(pickFileIntent)
                        }
                    }

                }
                is IntentType.CameraImage -> {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                null
                            }
                            photoFile?.also {
                                val photoUri: Uri =
                                    FileProvider.getUriForFile(
                                        requireActivity(),
                                        intentType.authority,
                                        it
                                    )
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                startCameraWithPermission(takePictureIntent)
                            }
                        }
                    }
                }
                is IntentType.CameraVideo -> {
                    Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                        takeVideoIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startCameraWithPermission(takeVideoIntent)
                        }
                    }
                }
                is IntentType.Other -> {
                    intentType.intent.resolveActivity(requireActivity().packageManager)?.also {
                        startActivityForResult(intentType.intent, K_REQUEST_CODE)
                    }
                }
                is IntentType.OverlayPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${requireActivity().packageName}")
                        ).also { overlayIntent ->
                            overlayIntent.resolveActivity(requireActivity().packageManager)?.also {
                                startActivityForResult(overlayIntent, K_REQUEST_CODE)
                            }
                        }
                    } else {}
                }
                is IntentType.WriteSettingsPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).also { settingIntent ->
                            settingIntent.data =
                                Uri.parse("package:${requireActivity().packageName}")
                            settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            settingIntent.resolveActivity(requireActivity().packageManager)?.also {
                                startActivityForResult(settingIntent, K_REQUEST_CODE)
                            }
                        }
                    } else {
                    }
                }
                is IntentType.AccessUsageSettingsPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).also { usageIntent ->
                            usageIntent.resolveActivity(requireActivity().packageManager)?.also {
                                startActivityForResult(usageIntent, K_REQUEST_CODE)
                            }
                        }
                    } else {
                    }
                }

                is IntentType.AccessibilitySettingsPermission -> Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).also { accIntent ->
                    accIntent.resolveActivity(requireActivity().packageManager)?.also {
                        startActivityForResult(accIntent, K_REQUEST_CODE)
                    }
                }
                is IntentType.EnableKeyboard -> Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).also { keyboardIntent ->
                    keyboardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    keyboardIntent.resolveActivity(requireActivity().packageManager)?.also {
                        startActivityForResult(keyboardIntent, K_REQUEST_CODE)
                    }
                }

            }
        } ?: run {
            fragmentManager?.beginTransaction()
                ?.remove(this)
                ?.commitAllowingStateLoss()
        }
    }

    private fun startContactWithPermission(intent: Intent) {
        askPermission(
            Manifest.permission.READ_CONTACTS
        ){
            startActivityForResult(intent, K_REQUEST_CODE)
        }.onDeclined {
            onPermissionResult(it)
        }
    }

    private fun startCameraWithPermission(intent: Intent){
        askPermission(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ){
            startActivityForResult(intent, K_REQUEST_CODE)
        }.onDeclined {
            onPermissionResult(it)
        }
    }

    private fun startIntentWithPermission(intent: Intent) {
        askPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ){
            startActivityForResult(intent, K_REQUEST_CODE)
        }.onDeclined {
            onPermissionResult(it)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDirectory = when (intentType) {
            is IntentType.CameraImage -> (intentType as IntentType.CameraImage).storageFileDirectory
            else -> null
        }
        storageDirectory?.also { if (!it.exists()) it.mkdirs() }
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDirectory ?: storageDir
        ).apply {
            cameraFilePath = absolutePath
        }
    }

    interface RuntimeFragmentListener{
        fun onResponse(resultCode: Int , intent: Intent?, permissionResult: PermissionResult?)
    }


}