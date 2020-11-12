package com.dev.amrafridi29.activityresultexample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab).setOnClickListener(v -> {
//            new KRuntimeFragment.Builder(this)
//                    .setIntentType(new KIntentType.KFile(KFileType.IMAGES ,false))
//                    .setOnCancelListener(kData -> {
//                    })
//                    .setOnResultListener(kData -> Log.e("TAG", "onCreate: "+kData.getIntent() ))
//                    .build();
        });
    }
}
