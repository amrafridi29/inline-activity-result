package com.dev.amrafridi29.activity.activityresult.model

import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract
import com.dev.amrafridi29.activity.activityresult.RuntimeFragment
import com.dev.amrafridi29.activity.getFile
import com.dev.amrafridi29.activity.permissions.PermissionResult
import java.io.File


data class ResultData(
    val resultCode: Int,
    val intent: Intent? = null,
    val runtimeFragment : RuntimeFragment,
    val permissionResult: PermissionResult?
){

    fun askAgain()= runtimeFragment.ask()
    fun hasDenied() = permissionResult?.hasDenied() ?: false
    fun hasForeverDenied() = permissionResult?.hasForeverDenied() ?: false
    fun goToSettings() = permissionResult?.goToSettings()
    fun askAgainPermission() = permissionResult?.askAgain()


    fun getSelectedFile() :File?{
        return try{
            intent?.data?.getFile(runtimeFragment.getActivity())
        }catch (ex: Exception){
            File(intent?.data.toString())
        }

    }

    fun getSelectedFiles(): MutableList<File>? {
        val activity = runtimeFragment.getActivity()
        activity ?: return null
        val clipData = intent?.clipData ?: return null
        val files = mutableListOf<File>()
        for(index in 0 until clipData.itemCount){
            clipData.getItemAt(index).apply {
                uri.getFile(activity)?.also {
                    files.add(it)
                }
            }

        }
        return files
    }

    fun getContact(columns: Array<String>) : MutableMap<String, String>{
        val activity = runtimeFragment.getActivity()
        val contact = mutableMapOf<String, String>()
        val contactUri = intent?.data
        contactUri ?: return contact
        activity ?: return contact
        val cursor: Cursor? = activity.contentResolver.query(
            contactUri, columns,
            null, null, null
        )
        cursor?.let { c->
            if(c.moveToFirst()){
                columns.forEach { column->
                    if(column== ContactsContract.Contacts._ID)
                        contact[column] = getContactNumber(c.getString(c.getColumnIndex(column)))
                    else
                        contact[column] =   c.getString(c.getColumnIndex(column))
                }

            }
        }
        cursor?.close()
        return contact
    }

    private fun getContactNumber(contactID: String): String{
        val activity = runtimeFragment.getActivity()
        var contactNumber = ""
        val cursorPhone: Cursor? = activity?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                    ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
            arrayOf<String>(contactID),
            null
        )

        if (cursorPhone?.moveToFirst()==true) {
            contactNumber =
                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
        }

        cursorPhone?.close()
        return contactNumber
    }

}