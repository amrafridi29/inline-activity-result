package com.dev.amrafridi29.activityresultexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.dev.amrafridi29.activity.activityresult.model.FileType
import com.dev.amrafridi29.activity.askFile
import kotlinx.android.synthetic.main.fragment_file.*

class FileFragment : Fragment(R.layout.fragment_file){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn.setOnClickListener {
            askFile(FileType.PDF){
                Toast.makeText(requireContext(), ""+it.getSelectedFile(), Toast.LENGTH_SHORT).show()
//                img.setImageURI(it.intent?.data)
            }.onCancel {
                AlertDialog.Builder(requireContext())
                    .setMessage("No file selected")
                    .setTitle("File")
                    .setNegativeButton("Cancel"){d, _ -> d.dismiss()}
                    .setPositiveButton("Ask Again"){d, _ -> d.dismiss()
                        it.askAgain()
                    }.show()
            }.onPermissionDenied {
                if(it.hasDenied())
                    Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()

                if(it.hasForeverDenied())
                    it.goToSettings()
            }
        }
    }
}