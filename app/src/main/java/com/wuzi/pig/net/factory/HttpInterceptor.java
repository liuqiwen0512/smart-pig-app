package com.wuzi.pig.net.factory;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.manager.LoginManager;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 拦截器可以添加公共参数 也可以输出请求链接
 */
public class HttpInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();
        RequestBody requestBody = request.body();
        String path = request.url().url().getPath();
        if (requestBody instanceof FormBody) {
            FormBody.Builder builder = new FormBody.Builder();
            FormBody body = (FormBody) requestBody;
            if (body.size() > 0) {
                for (int i = 0; i < body.size(); i++) {
                    builder.addEncoded(body.encodedName(i), body.encodedValue(i));
                }
            }
            // 加公共参数
//                builder.add("type", "Android");
            newBuilder.method(request.method(), builder.build());
        }
        if (LoginManager.isLogin()) {
            newBuilder.addHeader(RequestParams.AUTHORIZATION, LoginManager.getToken());
        }
        Request build = newBuilder.build();
        String log = "Request method== " + build.method() + " , url== " + build.url() + "?" + bodyToString(build.body());
        LogUtils.d(log);
        return chain.proceed(build);
    }
//http://211.157.135.194:8762/business/getAlarmList?os=android&size=20&page=1&type=LRA-AL00&brand=HONOR&version=10&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxIiwicGFzc3dvcmQiOiJhZG1pbkAxMjMiLCJleHAiOjE2NTk4NDQyOTcsInVzZXJuYW1lIjoiYWRtaW4ifQ.OPjR2Nf0vX-gGzkVIvCdHjCzSDefEAjnYG7rGFRYUJ8
    /**
     * 将公共参数拼装
     */
    private String bodyToString(RequestBody request) {
        try {
            final RequestBody copy = request;
            if (StringUtils.equals(copy.contentType().type(), "multipart")) {
                return "\n<image body>";
            }
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final Exception e) {
            return "";
        }
    }
}
