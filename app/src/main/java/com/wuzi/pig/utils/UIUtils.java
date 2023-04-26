package com.wuzi.pig.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.wuzi.pig.base.BaseApplication;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * dp 转换
 */
public class UIUtils {
    public static final String TAG = UIUtils.class.getSimpleName();

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * dp 转 px
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp 转 px
     */
    public static int sp2px(float spValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * 开启键盘
     */
    public static void openBroad(Context mContext, View view) {
        if (mContext == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    /**
     * 关闭键盘
     */
    public static void closeBroad(Context mContext, View view) {
        if (mContext == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取App的版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager mPackageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取App的版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager mPackageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context 去除了刘海屏 高度
     */
    public static int[] getScreenDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        LogUtils.d(TAG, " getScreenDisplay width = " + width + " height = " + height);
        return result;
    }

    public static int[] getScreenDisplay() {
        return getScreenDisplay(BaseApplication.getApplication());
    }

    /**
     * 获取屏幕分辨率
     * 包括刘海屏高度
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getRealSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getRealSize(outSize);
        LogUtils.d(TAG, " getScreenDisplay width = " + outSize.x + " height = " + outSize.y);
        return outSize;
    }

    /**
     * 获取屏幕分辨率
     * 包括刘海屏高度
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        LogUtils.d(TAG, " getDisplayMetrics width = " + outMetrics.widthPixels + " height = " + outMetrics.heightPixels);
        return outMetrics;
    }

    public static int getScreenWidth(Context context) {
        int[] screenDisplay = getScreenDisplay(context);
        return screenDisplay[0];
    }

    public static Point getScreenRealSize(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getRealSize(context);
        } else {
            int[] screenDisplay = getScreenDisplay();
            return new Point(screenDisplay[0], screenDisplay[1]);
        }
    }

    /**
     * 设置屏幕方向
     *
     * @param context
     * @param isLand
     */
    public static void setRequestedOrientation(Activity context, boolean isLand) {
        if (context == null) return;
        if (isLand) {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 判断当前设备是否是平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void setDrawableTint(AppCompatImageView imageView, int tintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageTintList(ColorStateList.valueOf(tintColor));
        }
    }

    public static void setDrawableSelectTint(AppCompatImageView imageView, boolean isSelect) {
        if (imageView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //imageView.setImageTintList(ColorStateList.valueOf(isSelect ? imageView.getContext().getResources().getColor(R.color.colorYellowBg) : imageView.getContext().getResources().getColor(R.color.white)));
        }
    }

    public static void doHuaweiToastCovered(Window window) {
        if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
            View focusView = window.getCurrentFocus();
            if (focusView == null) return;
            if (focusView instanceof EditText) {
                EditText editText = (EditText) focusView;
                int inputType = editText.getInputType();
                if (inputType == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT)
                        || inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    closeBroad(BaseApplication.getApplication(), editText);
                }
            }
        }
    }

    public static void fullScreen(Activity activity, boolean full) {
        fullScreen(activity.getWindow(), full);
    }

    public static void fullScreen(Window window, boolean full) {
        if (full) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = window.getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(attr);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
