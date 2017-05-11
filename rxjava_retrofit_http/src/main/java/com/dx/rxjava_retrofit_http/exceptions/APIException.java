package com.dx.rxjava_retrofit_http.exceptions;

import com.dx.rxjava_retrofit_http.HttpResult;

/**
 * Created by duanx on 2016/12/20.
 * 自定义异常，当接口返回的{@link HttpResult#statusCode}不为{@link com.dx.rxjava_retrofit_http.HttpConst # HTTP_OK}时，需要跑出此异常
 * eg：登陆时验证码错误；参数为传递等
 */

public class APIException extends Exception {
    public String code;
    public String message;

    public APIException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}