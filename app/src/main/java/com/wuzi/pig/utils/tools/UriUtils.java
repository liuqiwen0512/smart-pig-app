package com.wuzi.pig.utils.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.wuzi.pig.base.BaseApplication;

import java.io.File;

public class UriUtils {

    public static Uri getProviderUri(String filePath) {
        Context context = BaseApplication.getApplication();
        File file = new File(filePath);
        Uri fileUri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = context.getPackageName() + ".fileprovider";
                fileUri = FileProvider.getUriForFile(context, authority, file);
            } else {
                fileUri = Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUri;
    }

}
