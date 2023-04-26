package com.wuzi.pig.net.factory;

import androidx.annotation.Nullable;

public class ResponseException extends RuntimeException {
    public int code;
    public String message;

    public ResponseException(int code) {
        this.code = code;
    }

    public ResponseException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public String getPromptMessage() {
        return message;
    }

    public boolean isEmpty() {
        return code == ERROR.RESULT_CODE_201;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseException getInstance(Throwable throwable) {
        return NetExceptionUtils.handleException(throwable);
    }

    /**
     * 约定异常
     */
    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 成功返回
         */
        public static final int RESULT_CODE_200 = 200;
        /**
         * 成功失败
         */
        public static final int RESULT_CODE_FAIL_1 = -1;

        /**
         * 成功返回, 但是数据为空
         */
        public static final int RESULT_CODE_201 = 201;
        /**
         * 不支持的请求方式
         */
        public static final int RESULT_CODE_400 = 400;
        /**
         * 未经授权的访问
         */
        public static final int RESULT_CODE_401 = 401;
        /**
         * 不可访问该资源
         */
        public static final int RESULT_CODE_403 = 403;
        /**
         * 请求消息体错误
         */
        public static final int RESULT_CODE_406 = 406;
        /**
         * 服务端错误
         */
        public static final int RESULT_CODE_500 = 500;
        /**
         * 在别处登录
         */
        public static final int RESULT_CODE_601 = 601;
        /**
         * 登录账号、密码错误
         */
        public static final int RESULT_CODE_602 = 602;
        /**
         * token失效
         */
        public static final int RESULT_CODE_603 = 603;
        /**
         * token过期
         */
        public static final int RESULT_CODE_604 = 604;
        /**
         * 无权限访问
         */
        public static final int RESULT_CODE_610 = 610;
    }
}
