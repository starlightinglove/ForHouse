package com.yorhp.forhouse.color;

import android.app.Application;

import toast.ToastUtil;

/**
 * @author Tyhj
 * @date 2019-11-06
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
    }
}
