package com.dx.rxjava_retrofit_http.subscribers;

import com.dx.rxjava_retrofit_http.utils.ILog;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by duanx on 2016/12/21.
 * 请求返回数据结果（字符串）
 */

public abstract class HttpSubscriber<T> extends Subscriber<T> {

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
        onFail(e);
    }

    @Override
    public void onNext(T t) {
        ILog.i("请求成功！");
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFail(Throwable e);
}
