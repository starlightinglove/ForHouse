package com.yorhp.forhouse;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.yorhp.recordlibrary.ScreenRecordUtil;

import androidx.appcompat.app.AppCompatActivity;
import permison.FloatWindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenRecordUtil.getInstance().screenShot(MainActivity.this, null);

        findViewById(R.id.btnStart).setOnClickListener((v) -> {
            if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
    }
}
