package com.wuzi.pig.net;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadUtil {

    public static MultipartBody.Part fileToPart(String path) {

        File file = new File(path);
        if (file == null) return null;
        RequestBody body = RequestBody.create(file, MediaType.parse(MimeTypeUtil.getMIMEType(path)));
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploadFile", path, body);
        return part;

    }
}
