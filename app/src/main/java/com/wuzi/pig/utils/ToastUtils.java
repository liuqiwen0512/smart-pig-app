package com.wuzi.pig.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.wuzi.pig.base.BaseApplication;

/**
 * 吐司相关工具类
 */
public class ToastUtils {
    private static Toast sToast;
    public static void show(int stringID) {
        show(BaseApplication.getApplication().getString(stringID), Toast.LENGTH_SHORT);
    }

    public static void show(int stringID, int gravity) {
        show(BaseApplication.getApplication().getString(stringID), Toast.LENGTH_SHORT, gravity);
    }

    public static void show(String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void showLong(String text) {
        show(text, Toast.LENGTH_LONG);
    }

    private static void show(final String text, final int duration) {
        show(text, duration, Gravity.NO_GRAVITY);
    }

    private static void show(final String text, final int duration, final int gravity) {
        if (TextUtils.isEmpty(text)) return;
        if (android.os.Process.myTid() == BaseApplication.getMainThreadId()) {
            showSingleToast(text, duration, gravity);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showSingleToast(text, duration, gravity);
                }
            });
        }
    }

    private synchronized static void showSingleToast(String text, int duration,  int gravity) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(BaseApplication.getApplication(), text, duration);
        sToast.setGravity(gravity, 0, 0);
        sToast.show();
    }

}