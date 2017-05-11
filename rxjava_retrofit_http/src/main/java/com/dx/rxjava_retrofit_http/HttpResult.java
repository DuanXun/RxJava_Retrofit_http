package com.dx.rxjava_retrofit_http;

/**
 * Created by duanx on 2016/12/6.
 * 网络请求结果对象
 */

public class HttpResult<T> {

    public String statusCode;
    public String message;
    public T data;
    public String extra;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isSuccess() {
        return statusCode.equals(HttpConst.HTTP_OK);
    }

    @Override
    public String toString() {
        return "{" +
                "statusCode:'" + statusCode + '\'' +
                ", message:'" + message + '\'' +
                ", data:" + data.toString() +
                ", extra:'" + extra + '\'' +
                '}';
    }
}
