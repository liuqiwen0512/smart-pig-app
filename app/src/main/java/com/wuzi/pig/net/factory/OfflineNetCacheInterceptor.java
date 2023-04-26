package com.wuzi.pig.net.factory;

import com.wuzi.pig.base.BaseApplication;
import com.wuzi.pig.utils.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 无网络缓存
 */
public class OfflineNetCacheInterceptor implements Interceptor {

    public OfflineNetCacheInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isNetworkAvailable(BaseApplication.getApplication())) {
            request = request
                    .newBuilder()
                    .cacheControl(new CacheControl
                            .Builder()
                            .maxStale(60, TimeUnit.SECONDS)
                            .onlyIfCached()
                            .build())
                    .build();
        }
        return chain.proceed(request);
    }
}
