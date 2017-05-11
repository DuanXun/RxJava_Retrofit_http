package com.dx.demo;


import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by duanx on 2017/5/11.
 */

public interface InfoService {
    public static String BASE_URL = "http://gank.io/api/history/content/";

    @retrofit2.http.GET("{num}/1")
    Observable<ResponseBody> getInfo(@retrofit2.http.Path("num") String num);
}
