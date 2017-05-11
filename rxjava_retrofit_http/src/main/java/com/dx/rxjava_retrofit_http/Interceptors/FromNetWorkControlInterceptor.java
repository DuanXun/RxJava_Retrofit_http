package com.dx.rxjava_retrofit_http.Interceptors;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by duanx on 2016/12/20.
 */

public class FromNetWorkControlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
