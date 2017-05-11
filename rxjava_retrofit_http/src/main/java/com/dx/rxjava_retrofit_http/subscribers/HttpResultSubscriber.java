package com.dx.rxjava_retrofit_http.subscribers;

import com.dx.rxjava_retrofit_http.HttpResult;
import com.dx.rxjava_retrofit_http.exceptions.APIException;
import com.dx.rxjava_retrofit_http.utils.ILog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by duanx on 2016/12/21.
 * 请求返回数据结果（数据对象）
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> {

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
            // ToastUtils.getInstance().showToast(e.getMessage());
            ILog.e(e.getMessage());
        }
        onFail(new Throwable("服务器访问出错了！"));
    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (t.isSuccess()) {
            ILog.i("请求成功！");
            onSuccess(t.getData());
        }else{
            ILog.e("请求失败:"+t.toString());
            onFail(new Throwable(t.getMessage()));}
    }

    public abstract void onSuccess(T t);

    public abstract void onFail(Throwable e);
}