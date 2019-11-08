package com.yorhp.forhouse.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.yorhp.forhouse.R;
import com.yorhp.forhouse.color.LabColorLike;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import java.util.ArrayList;
import java.util.List;

import toast.ToastUtil;

/**
 * @author Tyhj
 * @date 2019-11-06
 */
public class HomeService extends BaseAccessbilityService {


    private static Integer[] y = {1000,1250, 1500, 1750, 2000, 2250, 2500};
    public static List<Integer> yPoint;

    static {
        yPoint = new ArrayList<>();
        for (Integer integer : y) {
            yPoint.add(integer);
        }
    }


    int xPoint = 1180;
    int red = Color.parseColor("#ee2e45");
    private static boolean pause = true;


    public static final String TAOBAO_PACKAGE_NAME = "com.taobao.taobao";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName().toString().equals(TAOBAO_PACKAGE_NAME)) {

        }
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        createWindowView();
    }


    private static Bitmap bitmap;

    private void start() {
        bitmap = ScreenRecordUtil.getInstance().getScreenShot();
        new Thread(() -> {
            LabColorLike labColorLike = new LabColorLike();
            for (int i = 0; i < yPoint.size(); i++) {
                if (pause) {
                    return;
                }
                //颜色为红色
                if (labColorLike.isLike(bitmap.getPixel(xPoint, yPoint.get(i)), red, 10)) {
                    clickOnScreen(xPoint, yPoint.get(i), 200, null);
                    SystemClock.sleep(500);
                    if (i == 0) {
                        //签到
                        continue;
                    } else {
                        SystemClock.sleep(3000);
                        //签到页面滑动
                        for (int k = 0; k < 5; k++) {
                            performScrollForward();
                            SystemClock.sleep(2000);
                        }
                        for (int k = 0; k < 3; k++) {
                            performScrollBackward();
                            SystemClock.sleep(1000);
                            performScrollForward();
                            SystemClock.sleep(1000);
                        }
                        SystemClock.sleep(1500);
                        //返回
                        performBackClick();
                        i--;
                    }
                    SystemClock.sleep(1000);
                    bitmap = ScreenRecordUtil.getInstance().getScreenShot();

                }
            }
            pause = true;
        }).start();
    }

    @Override
    public void onInterrupt() {

    }


    WindowManager.LayoutParams params;
    WindowManager windowManager;
    ImageView btnView;
    public static final int FLAG_LAYOUT_INSET_DECOR = 0x00000200;

    private void createWindowView() {
        btnView = new ImageView(getApplicationContext());
        btnView.setImageResource(R.drawable.ic_star);
        windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 设置Window Type
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        // 设置悬浮框不可触摸
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | FLAG_LAYOUT_INSET_DECOR;
        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
        params.format = PixelFormat.RGBA_8888;
        // 设置悬浮框的宽高
        params.width = 150;
        params.height = 150;
        params.gravity = Gravity.TOP;
        params.x = 300;
        params.y = 200;

        btnView.setOnTouchListener(new View.OnTouchListener() {

            //保存悬浮框最后位置的变量
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        windowManager.updateViewLayout(btnView, params);
                        break;
                }
                return false;
            }
        });
        btnView.setOnClickListener(clickListener);
        windowManager.addView(btnView, params);
    }


    View.OnClickListener clickListener = v -> {
        if (pause) {
            ToastUtil.showLong("点击星星暂停");
            pause = false;
            start();
        } else {
            ToastUtil.showShort("已暂停");
            pause = true;
        }
    };

}
