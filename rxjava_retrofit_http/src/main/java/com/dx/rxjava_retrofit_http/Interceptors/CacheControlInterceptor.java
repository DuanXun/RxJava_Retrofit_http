package com.dx.rxjava_retrofit_http.Interceptors;

import android.app.Application;
import android.util.Log;

import com.dx.rxjava_retrofit_http.utils.HttpUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by duanx on 2016/12/20.
 * 缓存数据拦截器
 */

public class CacheControlInterceptor implements Interceptor {

    private Application app;

    public CacheControlInterceptor(Application app) {
        this.app = app;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(!HttpUtils.isNetworkAvailable(app)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)//当无网络时默认读取缓存
                    .build();
            Log.e("HttpManager","暂无网络！");
        }

        Response response = chain.proceed( request);
        if(!HttpUtils.isNetworkAvailable(app)){
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }else{
            int maxAge = 60 * 60; // read from cache for 1 hour
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();

        }
        // re-write response header to force use of cache
        // 正常访问同一请求接口（多次访问同一接口），给30秒缓存，超过时间重新发送请求，否则取缓存数据
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(3, TimeUnit.SECONDS )
                .build();

        return response.newBuilder()
                .header("Cache-Control", cacheControl.toString() )
                .build();
    }
}
