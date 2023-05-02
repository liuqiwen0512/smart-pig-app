package com.wuzi.pig.utils.QR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wuzi.pig.utils.tools.IntentUtils;

public class QRActivityResultContract<I, O> extends ActivityResultContract<I, O> {

    private Activity mActivity;

    public QRActivityResultContract(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, I input) {
        return IntentUtils.getQRIntent(mActivity);
    }

    @Override
    public O parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(resultCode, intent);
            final String qrContent = scanResult.getContents();
            return (O) qrContent;
        }
        return null;
    }
}
