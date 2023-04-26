package com.wuzi.pig.entity;

/**
 * 网络响应基类
 *
 * {
 *     "code": 200,
 *     "msg": null,
 *     "data": {
 *         "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VyX2tleSI6IjBjNzAyNDFjLTBmYjYtNGI4Ni04MDUyLWQwNzZkMTYwYzRhNSIsInVzZXJuYW1lIjoiYWRtaW4ifQ.ANpd0f-y5aF2ugSsX3sknwbtcl44BmSjgZQnsT6oCPKSKcBTEJ8_od_k3Lry1V5gsj64Nbe7Vah_Dn28sjZZ1A",
 *         "expires_in": 10080
 *     }
 * }
 *
 */
public class ResponseEntity<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
