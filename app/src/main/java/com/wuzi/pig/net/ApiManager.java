package com.wuzi.pig.net;

import com.wuzi.pig.base.BaseApplication;
import com.wuzi.pig.constant.Constant;
import com.wuzi.pig.net.factory.CustomizeConverterFactory;
import com.wuzi.pig.net.factory.HttpInterceptor;

import java.io.File;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * 网络请求管理类
 */
public class ApiManager {
    // OkHttp 配置
    public static final Long READ_TIME_OUT = 1L;
    public static final Long WRITE_TIME_OUT = 1L;
    public static final Long CONNECT_TIME_OUT = 3L;
    private static volatile ApiSercice apiSercice;

    public ApiManager() {
        File netCacheDir = new File(BaseApplication.getApplication().getCacheDir(), Constant.NET_CACHE_DIR);
        if (!netCacheDir.exists()) {
            netCacheDir.mkdirs();
        }
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(netCacheDir, cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MINUTES)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MINUTES)
                .cache(cache)
                // addNetInterceptor是添加网络拦截器，
                // addInterceptor是添加应用拦截器，应用拦截器是在网络拦截器前执行的。
                .addNetworkInterceptor(new HttpInterceptor())
                //.addInterceptor(new MyHttpInterceptor())
                //.addNetworkInterceptor(new NetCacheInterceptor())
                //.addInterceptor(new OfflineNetCacheInterceptor())
                .proxy(Proxy.NO_PROXY)
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .addConverterFactory(CustomizeConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();
        apiSercice = retrofit.create(ApiSercice.class);
    }

    public static ApiSercice getApiService() {
        if (apiSercice == null) {
            synchronized (ApiManager.class) {
                if (apiSercice == null) {
                    new ApiManager();
                }
            }
        }
        return apiSercice;
    }

}
