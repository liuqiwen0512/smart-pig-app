package com.wuzi.pig.net;

import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.net.entity.ResponsePigFarmEntity;
import com.wuzi.pig.net.factory.ResponseNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Observable<ResponseEntity<Object>> deletePigfarm(@Path("pigfarmIds")String pigfarmIds);

}

