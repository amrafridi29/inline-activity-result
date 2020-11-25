package com.dev.amrafridi29.activityresultexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.amrafridi29.activity.activityresult.RuntimeFragment;
import com.dev.amrafridi29.activity.activityresult.callbacks.OnCancelCallback;
import com.dev.amrafridi29.activity.activityresult.callbacks.OnPermissionDeniedCallback;
import com.dev.amrafridi29.activity.activityresult.callbacks.OnResultCallback;
import com.dev.amrafridi29.activity.activityresult.model.FileType;
import com.dev.amrafridi29.activity.activityresult.model.IntentType;
import com.dev.amrafridi29.activity.activityresult.model.ResultData;

import org.jetbrains.annotations.NotNull;


public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab).setOnClickListener(v -> {
            RuntimeFragment.askResult(this, new IntentType.KFile(FileType.IMAGES , false))
                    .onResult(resultData -> {
                        //resulting data or intent here your action
                    })
                    .onCancel(resultData -> {
                        //ON_RESULT_CANCELED
                        //you can ask a file again by calling
                        //resultData.askAgain() or other actions
                    })
                    .onPermissionDenied(resultData -> {
                        //the list of denied permissions
                        if(resultData.hasDenied()){
                            //You can ask for permission again by calling
                            //resultData.askAgainPermission()
                        }

                        //the list of forever denied permissions, user has check 'never ask again'
                        if(resultData.hasForeverDenied()){
                            // you need to open setting manually if you really need it
                           // resultData.goToSettings();
                        }
                    }).ask();

        });
    }
}
