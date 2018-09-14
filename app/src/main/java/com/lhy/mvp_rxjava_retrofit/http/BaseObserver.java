package com.lhy.mvp_rxjava_retrofit.http;

import android.net.ParseException;
import android.text.TextUtils;
import android.widget.Toast;

import com.lhy.mvp_rxjava_retrofit.http.exception.ApiException;
import com.lhy.mvp_rxjava_retrofit.util.NetUtil;
import com.lhy.mvp_rxjava_retrofit.util.StringUtils;
import com.lhy.mvp_rxjava_retrofit.util.Utils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * @author Administrator
 * @date 2018/6/13
 */

public abstract class BaseObserver<T> extends ResourceObserver<T> {

    private boolean isShowErrorToast;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int GATEWAY_TIMEOUT = 504;

    public BaseObserver(boolean isShowErrorToast) {
        this.isShowErrorToast = isShowErrorToast;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onComplete() {

    }


    @Override
    public void onError(Throwable e) {
        onFail(e);
        onCompleted(false, null, e);

    }

    @Override
    public void onNext(T t) {
        onCompleted(true, t, null);
    }

    protected abstract void onCompleted(boolean isSuccess, T t, Throwable e);

    /**
     * 需要自己定义错误类型就重写onFail方法
     *
     * @param e
     */
    private void onFail(Throwable e) {
        if (NetUtil.isNetworkAvailable(Utils.getContext())) {
            if (isShowErrorToast) {
                String errorMsg;
                if (e instanceof HttpException) {
                    /** 网络异常，http 请求失败，即 http 状态码不在 [200, 300) 之间, such as: "server internal error". */
                    switch (((HttpException) e).code()) {
                        case FORBIDDEN:
                            errorMsg = "服务器已经理解请求，但是拒绝执行它";
                            break;
                        case NOT_FOUND:
                            errorMsg = "服务器异常，请稍后再试";
                            break;
                        case REQUEST_TIMEOUT:
                            errorMsg = "请求超时";
                            break;
                        case GATEWAY_TIMEOUT:
                        case INTERNAL_SERVER_ERROR:
                            errorMsg = "服务器遇到了一个未曾预料的状况，无法完成对请求的处理";
                            break;
                        default:
                            errorMsg = "网络错误";
                            break;
                    }
                } else if (e instanceof ApiException) {
                    /** 网络正常，http 请求成功，服务器返回逻辑错误 */
                    if (((ApiException) e).getMsg() != null && (!((ApiException) e).getMsg().equals("")))
                        errorMsg = ((ApiException) e).getMsg();
                    else errorMsg = "code:" + ((ApiException) e).getCode();
                } else if (e instanceof JsonParseException
                        || e instanceof JSONException
                        || e instanceof ParseException) {
                    errorMsg = "解析错误";
                } else if (e instanceof ConnectException) {
                    errorMsg = "连接失败";
                } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
                    errorMsg = "证书验证失败";
                } else if (e instanceof java.net.SocketTimeoutException) {
                    errorMsg = "连接超时";
                } else if (e instanceof java.net.UnknownHostException) {
                    errorMsg = "网络中断，请检查网络状态！";
                } else {
                    /** 其他未知错误 */
                    errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "其他未知错误";
                }
                if (StringUtils.isBlank(errorMsg)) errorMsg = "未知错误";
                Toast.makeText(Utils.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Utils.getContext(), "请检查您的网络", Toast.LENGTH_SHORT).show();
        }
    }


}
