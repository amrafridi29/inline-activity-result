package com.dev.amrafridi29.activityresultexample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dev.amrafridi29.activity.*
import com.dev.amrafridi29.activity.activityresult.model.FileType
import com.dev.amrafridi29.activity.exception.PermissionException
import com.dev.amrafridi29.activity.exception.RuntimeFragmentException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT){
                try{
                    val result = askFile(FileType.IMAGES)
                    img.setImageURI(result.intent?.data)
                }catch (e : RuntimeFragmentException){
                    if(e.isCanceled()){
                        AlertDialog.Builder(this@MainActivity)
                            .setMessage("No file selected")
                            .setTitle("File")
                            .setNegativeButton("Cancel"){d, _ -> d.dismiss()}
                            .setPositiveButton("Ask Again"){d, _ -> d.dismiss()
                                e.askAgain()
                            }.show()
                    }

                    if(e.hasDenied())
                        Toast.makeText(this@MainActivity, "Denied", Toast.LENGTH_SHORT).show()

                    if(e.hasForeverDenied()){
                        e.goToSettings()
                    }
                }
            }
//            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT){
//                try{
//                    val result = askPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
//                    Toast.makeText(this@MainActivity, "Granted", Toast.LENGTH_SHORT).show()
//                }catch (e: PermissionException){
//                    if(e.hasDenied())
//                        Toast.makeText(this@MainActivity, "Denied", Toast.LENGTH_SHORT).show()
//                    if(e.hasForeverDenied()){
//                        e.goToSettings()
//                    }
//                }
//            }
//            askEnableKeyboard  { resultData->
//                //Keyboard setting result
//            }.onCancel {resultData->
//                //ON_RESULT_CANCELED
//                //resultData.askAgain() or do your actions
//            }.onPermissionDenied {resultData->
//
//                //the list of denied permissions
//                if(resultData.hasDenied()){
//                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
//                    //You can ask for permission again by calling
//                    //resultData.askAgainPermission()
//                }
//                //the list of forever denied permissions, user has check 'never ask again'
//                if(resultData.hasForeverDenied()){
//                    // you need to open setting manually if you really need it
//                    resultData.goToSettings()
//                }
//            }

//            askFile(FileType.IMAGES){
//                img.setImageURI(it.intent?.data)
//            }.onCancel {
//                AlertDialog.Builder(this)
//                    .setMessage("No file selected")
//                    .setTitle("File")
//                    .setNegativeButton("Cancel"){d, _ -> d.dismiss()}
//                    .setPositiveButton("Ask Again"){d, _ -> d.dismiss()
//                        it.askAgain()
//                    }.show()
//            }.onPermissionDenied {
//                it.askAgainPermission()
//                if(it.hasDenied())
//                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
//
//                if(it.hasForeverDenied())
//                    it.goToSettings()
//            }
//            kaskPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION){
//                Toast.makeText(this, "Accepeted", Toast.LENGTH_SHORT).show()
//            }.onDeclined {
//                if(it.hasDenied()){
//                    Toast.makeText(this, "Dendied", Toast.LENGTH_SHORT).show()
//                }
//
//                if(it.hasForeverDenied()){
//                    it.goToSettings()
//                }
//            }
//            KRuntimePermission.askPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
//                .onResponse(object : KResponseCallback {
//                    override fun onResponse(result: KPermissionResult) {
//                        Log.e("TAG", "onResponse: $result")
//                    }
//
//                }).onAccepted(object : KAcceptedCallback {
//                    override fun onAccepted(result: KPermissionResult) {
//                        Log.e("TAG", "onAccepted: $result" )
//                    }
//
//                }).onDenied(object : KDeniedCallback {
//                    override fun onDenied(result: KPermissionResult) {
//                        Log.e("TAG", "onDenied: $result")
//                    }
//                }).onForeverDenied(object : KForeverDeniedCallback {
//                    override fun onForeverDenied(result: KPermissionResult) {
//                        Log.e("", "onForeverDenied: $result" )
//                    }
//
//                }).ask()
//            askFile(KFileType.IMAGES){
//                img.setImageURI(it.intent?.data)
//            }.onCancel{
//                Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show()
//            }.onPermissionDenied { kData, permissionResult ->
//                Toast.makeText(this, "Permission is denied try again", Toast.LENGTH_SHORT).show()
//            }
//            askOverlayPermission {
//                AlertDialog.Builder(this)
//                    .setMessage("Permission is granted")
//                    .setTitle("Granted")
//
//                    .setPositiveButton("Ok"){dialog , which->
//                        dialog.dismiss()
//                    }.show()
//            }.onCancel {
//                AlertDialog.Builder(this)
//                    .setMessage("Permission is denied")
//                    .setTitle("Ask again")
//                    .setNegativeButton("Cancel"){dialog,which->
//                        dialog.dismiss()
//                    }
//                    .setPositiveButton("Ask Again"){dialog , which->
//                        it.askAgain()
//                        dialog.dismiss()
//                    }.show()
//            }

//            askContact {
//                Log.e("TAG", "onCreate: "+it.getContact(arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME)))
//            }.onCancel {
//                AlertDialog.Builder(this)
//                    .setMessage("Contact not selected")
//                    .setTitle("Ask again")
//                    .setNegativeButton("Cancel"){ dialog, which->
//                        dialog.dismiss()
//                    }
//                    .setPositiveButton("Ask Again"){ dialog, which->
//                        it.askAgain()
//                        dialog.dismiss()
//                    }.show()
//            }.onPermissionDenied { kData, permissionResult ->
//                if(permissionResult.hasDenied()){
//                    AlertDialog.Builder(this)
//                        .setMessage("Access to contact is required")
//                        .setTitle("Contact Permission")
//                        .setNegativeButton("Cancel"){ dialog, which->
//                            dialog.dismiss()
//                        }
//                        .setPositiveButton("Ask Again"){ dialog, which->
//                            permissionResult.askAgain()
//                            dialog.dismiss()
//                        }.show()
//                }
//
//                if(permissionResult.hasForeverDenied())
//                    permissionResult.goToSettings()
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}