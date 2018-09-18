package com.lhy.mvp_rxjava_retrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lhy.mvp_rxjava_retrofit.http.BaseObserver;
import com.lhy.mvp_rxjava_retrofit.http.RxUtils;
import com.lhy.mvp_rxjava_retrofit.http.entity.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/25.
 */

public abstract class BaseActivity extends AppCompatActivity {


    protected CompositeDisposable compositeDisposable;
    private ProgressDialog mProgressDialog;
    protected boolean isRunning = true;// 该activity是在运行（未被销毁）true：在运行 false：已销毁

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initView();
    }


    /**
     * 返回data所有数据
     *
     * @param showDialogLoading 是否显示加载dialog
     * @param isShowErrorToast  是否显示错误的Toast提示
     */
    protected <T> void getDataAll(Observable<BaseResponse<T>> observable, int request_code, boolean showDialogLoading, boolean isShowErrorToast, final BaseObserver baseObserver) {
        addSubscribe(observable.compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleAll())
                .subscribeWith(new BaseObserver<BaseResponse<T>>(isShowErrorToast) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (showDialogLoading) showLoading("加载中...");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        //if (showDialogLoading) dismissLoading();
                    }


                    @Override
                    protected void onCompleted(boolean isSuccess, BaseResponse<T> data, Throwable e) {
                        if (isSuccess) {
                            if (baseObserver != null) baseObserver.onNext(data);
                        } else {
                            if (baseObserver != null) baseObserver.onError(e);
                        }
                        if (showDialogLoading) dismissLoading();
                    }

                }));
    }

    /**
     * 返回剥去code的result
     *
     * @param showDialogLoading 是否显示加载dialog
     * @param isShowErrorToast  是否显示错误的Toast提示
     */
    protected <T> void getDataResult(Observable<BaseResponse<T>> observable, int request_code, boolean showDialogLoading, boolean isShowErrorToast, final BaseObserver baseObserver) {
        addSubscribe(observable.compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<T>(isShowErrorToast) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (showDialogLoading) showLoading("加载中...");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        //  if (showDialogLoading) dismissLoading();
                    }

                    @Override
                    protected void onCompleted(boolean isSuccess, T t, Throwable e) {
                        if (isSuccess) {
                            if (baseObserver != null) baseObserver.onNext(t);
                        } else {
                            if (baseObserver != null) baseObserver.onError(e);
                        }
                        if (showDialogLoading) dismissLoading();
                    }
                }));
    }


    /**
     * 封装到base里面效果最佳
     *
     * @param disposable disposable
     */
    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    private void showLoading(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载中...");
            mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        mProgressDialog.setMessage(content);
        if (isRunning) {
            mProgressDialog.show();
        }
    }

    private void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    protected abstract void initView();

    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void finish() {
        super.finish();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
