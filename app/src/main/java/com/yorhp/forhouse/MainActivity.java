package com.yorhp.forhouse;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.example.recyclelibrary.CommonAdapter;
import com.example.recyclelibrary.CommonViewHolder;
import com.yorhp.forhouse.service.HomeService;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import permison.FloatWindowManager;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcylView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenRecordUtil.getInstance().screenShot(MainActivity.this, null);

        rcylView = findViewById(R.id.rcylView);
        rcylView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcylView.setAdapter(new CommonAdapter(this, HomeService.yPoint, R.layout.item_count) {
            @Override
            public void onBindView(CommonViewHolder holder, Object o) {
                holder.setText(R.id.tvCount, String.valueOf(holder.getAdapterPosition()));
                holder.setOnClick(R.id.tvCount, (v) -> {
                    removeData(holder.getAdapterPosition());
                });
            }
        });
        findViewById(R.id.btnStart).setOnClickListener((v) -> {
            if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
    }
}
