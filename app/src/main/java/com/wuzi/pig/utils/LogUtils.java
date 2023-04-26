package com.wuzi.pig.utils;

import android.util.Log;

import com.wuzi.pig.constant.Constant;


/**
 * log 工具类，统一去调用
 */
public class LogUtils {

    private static final String TAG = "FFNR_LOG: ";

    public static void v(String st) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG, init(st));
        }
    }

    public static void v(int num) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG, init(String.valueOf(num)));
        }
    }

    public static void v(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG + tag, init(st));
        }
    }

    public static void v(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG + tag, init(String.valueOf(num)));
        }
    }

    public static void d(String st) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG, init(st));
        }
    }

    public static void d(int num) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG, init(String.valueOf(num)));
        }
    }

    public static void d(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG + tag, init(st));
        }
    }

    public static void d(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG + tag, init(String.valueOf(num)));
        }
    }

    public static void e(String st) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG, init(st));
        }
    }

    public static void e(int num) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG, init(String.valueOf(num)));
        }
    }

    public static void e(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG + tag, init(st));
        }
    }

    public static void e(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG + tag, init(String.valueOf(num)));
        }
    }

    public static void w(String st) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG, init(st));
        }
    }

    public static void w(int num) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG, init(String.valueOf(num)));
        }
    }

    public static void w(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG + tag, init(st));
        }
    }

    public static void w(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG + tag, init(String.valueOf(num)));
        }
    }

    /**
     * 增加额外的显示
     */
    public static String init(String string) {
        StringBuilder st = new StringBuilder();
//        StringBuffer st = new StringBuffer();
//        st.append("\r\n-- start -------------\r\n");
//        st.append("//\n");
        st.append(string);
//        st.append("\r\n-- end ---------------\r\n");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String endTime = LocalDateTime.now().format(formatter);
//            st.append("endTime == ").append(endTime);
//        }
        return st.toString();
    }
}
