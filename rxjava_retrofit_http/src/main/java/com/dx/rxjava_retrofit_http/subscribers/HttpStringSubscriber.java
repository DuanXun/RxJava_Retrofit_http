package com.dx.rxjava_retrofit_http.subscribers;

import com.dx.rxjava_retrofit_http.utils.ILog;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import okhttp3.ResponseBody;

/**
 * Created by duanx on 2016/12/25.
 */

public abstract class HttpStringSubscriber extends Subscriber<ResponseBody>{

    @Override
    public void onStart() {
        super.onStart();
        ILog.i("请求开始！");
    }

    @Override
    public void onCompleted() {
        ILog.i("请求完成！");
    }

    @Override
    public void onError(Throwable e) {
        ILog.e("请求失败!");
        e.printStackTrace();
        //在这里做全局的错误处理
        if (e instanceof HttpException) {
            ILog.e(e.getMessage());
        }
        onFail(new Throwable("网络访问异常！"));
    }

    @Override
    public void onNext(ResponseBody r) {
        ILog.i("请求成功！");
        try {
            String s = new String(r.bytes());
            onSuccess(s);
        } catch (IOException e) {
            e.printStackTrace();
            onFail(new Throwable("服务器数据异常！"));
        }

    }

    public abstract void onSuccess(String result);

    public abstract void onFail(Throwable e);
}
