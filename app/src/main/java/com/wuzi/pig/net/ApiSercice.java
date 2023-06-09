package com.wuzi.pig.net;

import com.wuzi.pig.entity.ActivityListEntity;
import com.wuzi.pig.entity.AlarmListEntity;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.entity.TempListEntity;
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
    @ResponseNull
    @PUT("/prod-api/system/user/profile/forgetPwd")
    Observable<ResponseEntity<UserEntity>> forgetPwd(@QueryMap HashMap<String, Object> map);

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

    //猪场绑定
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/prod-api/system/bing")
    Observable<ResponseEntity<Object>> bindPigfarm(@Body HashMap<String, Object> map);

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

    //猪栏添加
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/prod-api/system/pigsty")
    Observable<ResponseEntity<Object>> addPigsty(@Body HashMap<String, Object> map);

    //猪栏修改
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @PUT("/prod-api/system/pigsty")
    Observable<ResponseEntity<Object>> updatePigsty(@Body HashMap<String, Object> map);

    //猪栏删除
    @ResponseNull
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @DELETE("/prod-api/system/pigsty/{pigstyIds}")
    Observable<ResponseEntity<Object>> deletePigsty(@Path("pigstyIds") String pigdtyIds);

    //告警list
    @GET("/prod-api/alarm/app/getAlarms")
    Observable<ResponseEntity<AlarmListEntity>> getAlarms(@QueryMap HashMap<String, Object> map);

    //监测猪栏list
    @GET("/prod-api/alarm/app/getPigstyAlarm")
    Observable<ResponseEntity<PigstyListEntity>> getPigstyAlarm(@QueryMap HashMap<String, Object> map);

    //监测猪栏count
    @GET("/prod-api/alarm/app/getPigsty")
    Observable<ResponseEntity<String>> getPigstyCount(@QueryMap HashMap<String, Object> map);

    //监测生猪count
    @GET("/prod-api/alarm/app/getEarTag")
    Observable<ResponseEntity<Statis72HourEntity>> getStatis72Hour(@QueryMap HashMap<String, Object> map);

    //监测生猪温度数据
    @GET("/prod-api/alarm/app/getTemperatures")
    Observable<ResponseEntity<ResponseListData<TempListEntity>>> getTemperatures(@QueryMap HashMap<String, Object> map);

    //监测生猪活跃度数据
    @GET("/prod-api/alarm/app/getMovements")
    Observable<ResponseEntity<ResponseListData<ActivityListEntity>>> getMovements(@QueryMap HashMap<String, Object> map);

}

