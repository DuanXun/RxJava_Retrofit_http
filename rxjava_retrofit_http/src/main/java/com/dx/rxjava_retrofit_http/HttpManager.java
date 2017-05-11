package com.dx.rxjava_retrofit_http;

import android.app.Application;

import com.dx.rxjava_retrofit_http.Interceptors.CacheControlInterceptor;
import com.dx.rxjava_retrofit_http.Interceptors.HttpLoggingInterceptor;
import com.dx.rxjava_retrofit_http.utils.ILog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by duanxun on 2016/5/26.
 * Describe:本项目网络请求封装类
 * Version: v1.0
 */
public class HttpManager {

    private OkHttpClient okHttpClient;
    private final Gson mGsonDateFormat;
    private Application app;
    private boolean isDebug;

    public HttpManager() {
        mGsonDateFormat = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }

    private static class Holder{
        private static final HttpManager httpManager = new HttpManager();
    }

    public static final HttpManager getInstance(){
        return Holder.httpManager;
    }

    /**
     * 网络管理模块初始化
     * @param isDebug     是否debug调试状态
     */
    public void init(final boolean isDebug,Application application){
        this.isDebug = isDebug;
        this.app = application;
        ILog.d("isDebug:"+isDebug);
    }

    /**
     * 创建接口访问载体
     * @param serviceClass
     * @param <S>
     * @return
     */
    public <S> S createService(Class<S> serviceClass) {
        String base_url = "";
        try {
            Field field1 = serviceClass.getField("BASE_URL");
            base_url = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            ILog.e(serviceClass.getSimpleName()+"没有标记 BASE_URL 的值！");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(okHttpClient==null){
            okHttpClient = clientProvider();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGsonDateFormat))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(base_url)
                .build();
        return retrofit.create(serviceClass);
    }

    /**
     * 创建拦截器，为网络访问添加公共的头文件参数
     * @return
     */
    private Interceptor getHeaderInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        .addHeader("debug", String.valueOf(isDebug))
                        .build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 创建网络访问端
     * @return
     */
    private OkHttpClient clientProvider(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                ILog.i("HttpManager",message);
            }
        });

        File httpCacheDirectory = new File(app.getExternalCacheDir(), "responses");//设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(getHeaderInterceptor())
                .addInterceptor(new CacheControlInterceptor(app))
                .addInterceptor(logInterceptor)
                .connectTimeout(HttpConst.HTTP_TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(HttpConst.HTTP_TIME_OUT,TimeUnit.SECONDS)
                .readTimeout(HttpConst.HTTP_TIME_OUT,TimeUnit.SECONDS)
                .cache(cache)
                .build();
        return client;
    }

}
