package com.wuzi.pig.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.wuzi.pig.R;
import com.wuzi.pig.utils.fun.Function;

import java.util.List;

/**
 * 权限获取工具类
 */
public class XXPermissionsUtils {

    private static volatile XXPermissionsUtils xxPermissionsUtils;
    public Context context;
    /*读写sd卡权限*/
    private String[] readOrWritePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] readOrWriteAndroid10Permission = new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE};

    /*拍照权限*/
    private String[] cameraPermission = new String[]{Manifest.permission.CAMERA};
    /*录音权限*/
    private String[] recordPermission = new String[]{Manifest.permission.RECORD_AUDIO};
    /*获取指纹信息权限*/
    private String[] fingerprintPermission = new String[]{Manifest.permission.USE_BIOMETRIC, Manifest.permission.USE_FINGERPRINT};
    /*拨打电话权限*/
    private String[] callPhonePermission = new String[]{Manifest.permission.CALL_PHONE};
    /*读取手机状态权限*/
    private String[] readPhonePermission = new String[]{Manifest.permission.READ_PHONE_STATE};
    /*读取手机位置状态权限*/
    private String[] locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    /*后台定位权限*/
    private String[] accessBackgroundLocationPermission = new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    /*弹框权限*/
    private String[] systemAlertPermission = new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW};
    /*写入设置权限*/
    private String[] writeSettingPermission = new String[]{Manifest.permission.WRITE_SETTINGS};
    /*附近蓝牙权限*/
    private String[] bluePermission = new String[]{Manifest.permission.BLUETOOTH_SCAN};

    public XXPermissionsUtils() {

    }

    public static XXPermissionsUtils getInstances() {
        if (xxPermissionsUtils == null) {
            synchronized (XXPermissionsUtils.class) {
                if (xxPermissionsUtils == null) {
                    xxPermissionsUtils = new XXPermissionsUtils();
                }
            }
        }
        return xxPermissionsUtils;
    }

    /**
     * 拍照权限
     */
    public void hasCameraPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, cameraPermission, readOrWritePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, cameraPermission, readOrWritePermission);
        }
    }

    /**
     * 拍照权限
     */
    public void hasCameraPermission(Runnable runnable, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, cameraPermission)) {
            runnable.run();
            this.context = null;
        } else {
            requestPermission(allow -> {
                if (allow) {
                    runnable.run();
                }
            }, cameraPermission);
        }
    }

    /**
     * 读写sd卡权限
     */
    public void hasReadAndWritePermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, readOrWritePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, readOrWritePermission);
        }
    }

    /**
     * 读写sd卡权限
     */
    public void hasReadAndWrite10Permission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, readOrWriteAndroid10Permission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, readOrWriteAndroid10Permission);
        }
    }

    /**
     * 读写sd卡权限
     */
    public void haslocation10Permission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, locationPermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, locationPermission);
        }
    }

    /**
     * 附近蓝牙权限
     */
    public void hasblue12Permission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, bluePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, bluePermission);
        }
    }

    /**
     * 录音权限
     */
    public void hasRecordPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, recordPermission, readOrWritePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, recordPermission, readOrWritePermission);
        }
    }

    /**
     * 指纹权限
     */
    public void hasFingerprintPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, fingerprintPermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, fingerprintPermission);
        }
    }

    /**
     * 是否有录音权限
     */
    public boolean hasRecordPermission(Context context) {
        if (XXPermissions.isGranted(context, recordPermission)) {
            this.context = null;
            return true;
        }
        return false;
    }

    /**
     * 拨打电话权限
     */
    public void hasCallPhonePermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, callPhonePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, callPhonePermission);
        }
    }

    /**
     * 读取手机状态权限
     */
    public void hasReadPhonePermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, readPhonePermission, readOrWritePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, readPhonePermission, readOrWritePermission);
        }
    }

    /**
     * 弹框权限
     */
    public void hasSystemAlertPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, systemAlertPermission, readOrWritePermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, systemAlertPermission, readOrWritePermission);
        }
    }

    /**
     * 后台定位权限
     * 如果申请的权限中包含后台定位权限， 那么这里面则不能包含和定位无关的权限，否则框架会抛出异常，因为 ACCESS_BACKGROUND_LOCATION 和其他非定位权限定位掺杂在一起申请，在 Android 11 上会出现不申请直接被拒绝的情况
     */
    public void hasBackgroundLocationPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, accessBackgroundLocationPermission)) {
            booleanFunction.action(true);
            this.context = null;
        } else {
            requestPermission(booleanFunction, accessBackgroundLocationPermission);
        }
    }

    /**
     * 读取必要状态权限
     */
    public void hasNeedPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, readPhonePermission, readOrWritePermission, cameraPermission, recordPermission, locationPermission)) {
            booleanFunction.action(true);
        } else {
            requestPermission(booleanFunction, readPhonePermission, readOrWritePermission, cameraPermission, recordPermission, locationPermission);
        }
    }

    /**
     * targetSdkVersion >= 30 ,则不能申请 READ_EXTERNAL_STORAGE 和 WRITE_EXTERNAL_STORAGE 权限，而是应该申请 MANAGE_EXTERNAL_STORAGE 权限
     * 读取必要状态权限
     */
    public void hasNeed10Permission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, locationPermission, readOrWritePermission, readPhonePermission)) {
            booleanFunction.action(true);
        } else {
            requestPermission(booleanFunction, locationPermission, readOrWritePermission, readPhonePermission);
        }
    }

    /**
     * 手机通话读取必要状态权限
     * 高通大屏 弹出悬浮窗时会闪退
     */
    public void hasMTPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isGranted(context, readPhonePermission, readOrWritePermission, cameraPermission, recordPermission, locationPermission)) {
            booleanFunction.action(true);
        } else {
            requestPermission(booleanFunction, readPhonePermission, readOrWritePermission, cameraPermission, recordPermission, locationPermission);
        }
    }

    private void requestPermission(final Function<Boolean> booleanFunction, String[]... permission) {
        Activity context = (Activity) this.context;
        if (context == null || context.isDestroyed()) return;
        XXPermissions.with(context)
                //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(permission) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            booleanFunction.action(true);
                        } else {
                            booleanFunction.action(false);
                        }
                        XXPermissionsUtils.this.context = null;
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            if (XXPermissionsUtils.this.context != null) {
                                ToastUtils.show(XXPermissionsUtils.this.context.getString(R.string.permissions_refuse_authorization));
                                XXPermissions.startPermissionActivity(XXPermissionsUtils.this.context);
                            }
                        } else {
                            if (XXPermissionsUtils.this.context != null) {
                                ToastUtils.show(XXPermissionsUtils.this.context.getString(R.string.permissions_rror));
                            }
                        }
                        booleanFunction.action(false);
                        XXPermissionsUtils.this.context = null;
                    }
                });
    }
}
