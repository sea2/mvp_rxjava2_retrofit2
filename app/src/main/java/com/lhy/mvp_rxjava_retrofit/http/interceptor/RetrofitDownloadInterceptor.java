package com.lhy.mvp_rxjava_retrofit.http.interceptor;

import com.lhy.mvp_rxjava_retrofit.http.down.RetrofitDownloadListener;
import com.lhy.mvp_rxjava_retrofit.http.down.RetrofitResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/22.
 */

public class RetrofitDownloadInterceptor implements Interceptor {

    private RetrofitDownloadListener downloadListener;

    public RetrofitDownloadInterceptor(RetrofitDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();

        //添加公共参数,添加到header中
        // 新的请求
        Request.Builder requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());
        //发现服务器会随机的对下发的资源做GZip操作，而此时就没有相应的content-length  -1的问题，能获取下载进度
        requestBuilder.header("Accept-Encoding", "identity");
        Request newRequest = requestBuilder.build();

        Response response = chain.proceed(newRequest);
        return response.newBuilder().body(new RetrofitResponseBody(response.body(), downloadListener)).build();
    }
}
