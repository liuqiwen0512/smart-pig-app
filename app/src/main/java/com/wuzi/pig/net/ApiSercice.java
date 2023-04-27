package com.wuzi.pig.net;

import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.net.factory.ResponseNull;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

/**
 * 接口类
 */
public interface ApiSercice {

    //
    @ResponseNull
    @GET("")
    Observable<ResponseEntity<Object>> appOnline(@QueryMap Map<String, Object> map);

    //软件更新检查
    @Streaming
    @GET("")
    Observable<ResponseEntity<Object>> checkNewUpdate(@QueryMap HashMap<String, Object> map);

    //短信验证码
    @GET("/prod-api/system/sms/getSend")
    Observable<ResponseEntity<Object>> getSend(@QueryMap Map<String, Object> map);

    //注册
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/prod-api/system/user/register")
    Observable<ResponseEntity<UserEntity>> register(@Body HashMap<String, Object> map);

    //忘记密码
    @PUT("/prod-api/system/user/profile/forgetPwd")
    Observable<ResponseEntity<UserEntity>> forgetPwd(@Body HashMap<String, Object> map);

    //登录
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/prod-api/auth/login")
    Observable<ResponseEntity<UserEntity>> login(@Body HashMap<String, Object> map);

    //猪场list
    @GET("/prod-api/system/pigfarm/list")
    Observable<ResponseEntity<PigFarmListEntity>> getPigFarmList(@QueryMap HashMap<String, Object> map);

    //猪场添加
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/prod-api/system/pigfarm")
    Observable<ResponseEntity<Object>> addPigfarm(@Body HashMap<String, Object> map);

    //猪场修改
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @PUT("/prod-api/system/pigfarm")
    Observable<ResponseEntity<Object>> updatePigfarm(@Body HashMap<String, Object> map);

    //猪场删除
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @DELETE("/prod-api/system/pigfarm/{pigfarmIds}")
    Observable<ResponseEntity<Object>> deletePigfarm(@Path("pigfarmIds") String pigfarmIds);

    //猪栏list
    @GET("/prod-api/system/pigsty/list")
    Observable<ResponseEntity<PigstyListEntity>> getPigstyList(@QueryMap HashMap<String, Object> map);

    //猪场添加
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/prod-api/system/pigsty")
    Observable<ResponseEntity<Object>> addPigsty(@Body HashMap<String, Object> map);

    //猪场修改
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @PUT("/prod-api/system/pigsty")
    Observable<ResponseEntity<Object>> updatePigsty(@Body HashMap<String, Object> map);

    //猪场删除
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @DELETE("/prod-api/system/pigsty/{pigstyIds}")
    Observable<ResponseEntity<Object>> deletePigsty(@Path("pigstyIds") String pigdtyIds);


}

