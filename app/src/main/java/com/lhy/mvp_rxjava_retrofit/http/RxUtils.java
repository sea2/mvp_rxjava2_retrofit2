package com.lhy.mvp_rxjava_retrofit.http;

import com.lhy.mvp_rxjava_retrofit.http.entity.BaseResponse;
import com.lhy.mvp_rxjava_retrofit.http.exception.ApiException;

import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/13.
 */

public class RxUtils {
    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return FlowableTransformer
     */
    public static <T> FlowableTransformer<T, T> rxFlSchedulerHelper() {
        return flowable -> flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 统一返回结果处理 返回处理后的result
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<BaseResponse<T>, T> handleResult() {
        return httpResponseObservable ->
                httpResponseObservable.flatMap((Function<BaseResponse<T>, Observable<T>>) baseResponse -> {
                    if (baseResponse.getCode() == 200) {
                        return createData(baseResponse.getResult());
                    } else {
                        return Observable.error(new ApiException(baseResponse.getCode(), baseResponse.getMsg()));
                    }
                });
    }

    /**
     * 统一返回结果处理 返回原始结果
     *
     * @param <T> 指定的泛型类型
     *
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> handleAll() {
        return upstream -> upstream;
    }

    /**
     * 统一返回结果处理 返回原始结果
     *
     * @return ObservableTransformer
     */
    public static ObservableTransformer<String, String> handleAllString() {
        return upstream -> upstream;
    }


    /**
     * 得到 Observable
     *
     * @param <T> 指定的泛型类型
     * @return Observable
     */
    private static <T> Observable<T> createData(final T t) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(t);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    /**
     * 泛型转换工具方法 eg:object ==> map<String, String>
     *
     * @param object Object
     * @param <T>    转换得到的泛型对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
