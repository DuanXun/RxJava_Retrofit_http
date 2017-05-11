package com.dx.demo;

import android.app.Application;

import com.dx.rxjava_retrofit_http.HttpManager;

/**
 * Created by duanx on 2017/5/11.
 */

public class App extends Application {

    private static App mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        HttpManager.getInstance().init(true,this);
        mApp = this;
    }
}
