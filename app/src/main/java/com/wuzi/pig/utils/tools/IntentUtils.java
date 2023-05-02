package com.wuzi.pig.utils.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.zxing.integration.android.IntentIntegrator;
import com.wuzi.pig.utils.QR.QRActivity;

public class IntentUtils {

    public static Intent getImageCaptureIntent(String filePath) {
        Uri uri = UriUtils.getProviderUri(filePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    public static Intent getTelephoneIntent(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getQRIntent(Activity activity) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setCaptureActivity(QRActivity.class); // 设置自定义的activity是QRActivity
        intentIntegrator.setRequestCode(IntentIntegrator.REQUEST_CODE);
        Intent scanIntent = intentIntegrator.createScanIntent();

        return scanIntent;
    }

}
