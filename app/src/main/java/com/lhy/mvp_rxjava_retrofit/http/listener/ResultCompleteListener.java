package com.lhy.mvp_rxjava_retrofit.http.listener;

/**
 * Created by lhy on 2018/9/14.
 */
public interface ResultCompleteListener<T> {


    void success(T t);

    void fail(Throwable e);
}
