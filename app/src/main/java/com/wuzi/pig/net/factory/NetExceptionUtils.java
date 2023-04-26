package com.wuzi.pig.net.factory;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.wuzi.pig.constant.Constant;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.NetworkUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class NetExceptionUtils {

    private static String TAG = NetExceptionUtils.class.getSimpleName();
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseException handleException(Throwable throwable) {
        ResponseException responseException;
        LogUtils.d(TAG, "Throwable: " + throwable.toString());
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            responseException = new ResponseException(throwable, ResponseException.ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
//                    responeThrowable.code = httpException.code();
                    responseException.message = "网络错误";
                    break;
            }
            return responseException;
        } else if (throwable instanceof ServerException) {
            ServerException resultException = (ServerException) throwable;
            responseException = new ResponseException(resultException, resultException.code);
            responseException.message = resultException.message;
            return responseException;
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            responseException = new ResponseException(throwable, ResponseException.ERROR.PARSE_ERROR);
            responseException.message = "解析错误";
            return responseException;
        } else if (throwable instanceof ConnectException) {
            responseException = new ResponseException(throwable, ResponseException.ERROR.NETWORD_ERROR);
            if (NetworkUtils.isNetworkAvailable()) {
                responseException.message = "连接失败";
            } else {
                responseException.message = "无网络";
            }
            return responseException;
        } else if (throwable instanceof javax.net.ssl.SSLHandshakeException) {
            responseException = new ResponseException(throwable, ResponseException.ERROR.SSL_ERROR);
            responseException.message = "证书验证失败";
            return responseException;
        } else if (throwable instanceof ResponseException) {
            return (ResponseException) throwable;
        } else if (throwable instanceof SocketTimeoutException) {
            responseException = new ResponseException(throwable, ResponseException.ERROR.NETWORD_ERROR);
            responseException.message = "网络超时";
            return responseException;
        } else {
            responseException = new ResponseException(throwable, ResponseException.ERROR.UNKNOWN);
            responseException.message = Constant.LOG_SWITCH ? throwable.getMessage() : "未知错误";
            return responseException;
        }
    }

    /**
     * ServerException发生后，将自动转换为ResponeThrowable返回
     */
    class ServerException extends RuntimeException {
        int code;
        String message;
    }
}
