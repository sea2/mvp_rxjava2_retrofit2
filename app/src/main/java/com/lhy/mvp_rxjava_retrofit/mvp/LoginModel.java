package com.lhy.mvp_rxjava_retrofit.mvp;

import com.lhy.mvp_rxjava_retrofit.common.ApiService;
import com.lhy.mvp_rxjava_retrofit.entity.DataTestInfo;
import com.lhy.mvp_rxjava_retrofit.http.BaseObserver;
import com.lhy.mvp_rxjava_retrofit.http.RetrofitServiceManager;
import com.lhy.mvp_rxjava_retrofit.http.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:延时模拟登陆（2s），如果名字或者密码为空则登陆失败，否则登陆成功
 */
public class LoginModel implements ILoginModel {

    private Disposable disposable = null;


    @Override
    public void login(HashMap map, LoadDataCallback listener) {
        //直接调用返回返回Result对象
        disposable = RetrofitServiceManager.getInstance().creat(ApiService.class)
                .postDataType(map)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<DataTestInfo>(true) {

                    @Override
                    protected void onCompleted(boolean isSuccess, DataTestInfo dataTestInfo, Throwable e) {
                        if (listener != null) listener.onComplete(isSuccess, dataTestInfo, e);
                    }
                });
    }

    @Override
    public void cancelTasks() {
        if (disposable != null) {
            disposable.dispose();
        }
    }


    public interface LoadDataCallback<T> {
        void onComplete(boolean isSuccess, T t, Throwable e);
    }


}
