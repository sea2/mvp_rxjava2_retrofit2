package com.lhy.mvp_rxjava_retrofit.http;

import android.util.Log;

import com.lhy.mvp_rxjava_retrofit.common.ApiService;
import com.lhy.mvp_rxjava_retrofit.http.interceptor.CommonParamsInterceptor;
import com.lhy.mvp_rxjava_retrofit.http.interceptor.HttpHeaderInterceptor;
import com.lhy.mvp_rxjava_retrofit.util.HttpsUtils;
import com.lhy.mvp_rxjava_retrofit.util.NetUtil;
import com.lhy.mvp_rxjava_retrofit.util.Utils;
import com.lhy.mvp_rxjava_retrofit.MyApplication;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * @author Administrator
 * @date 2018/3/24
 */

public class RetrofitServiceManager {

    private static final int DEFAULT_TIME_OUT = 10;//超时时间

    //设缓存有效期为30天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 30;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    private static RetrofitServiceManager mRetrofitServiceManager;
    private Retrofit mRetrofit;


    private RetrofitServiceManager() {

        /**
         * 设置证书的三种方式
         */
        /**
         * 设置可访问所有的https网站
         */
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                其他配置
//                .build();

        /**
         *  设置具体的证书
         */

//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager))
//        //其他配置
//         .build();

        /**双向认证
         *
         */
//        HttpsUtils.getSslSocketFactory(
//                证书的inputstream,
//                本地证书的inputstream,
//                本地证书的密码)


        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.retryOnConnectionFailure(false);//连接失败后是否重新连接
        okHttpClientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        //设置支持所有https请求
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okHttpClientBuilder.hostnameVerifier((hostname, session) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        okHttpClientBuilder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getInstance())));

        addInterceptor(okHttpClientBuilder);


        mRetrofit = new Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .baseUrl(ApiService.BASR_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


    }


    /**
     * 添加各种拦截器
     *
     * @param builder
     */
    private void addInterceptor(OkHttpClient.Builder builder) {
        //公共Header参数插值器
        builder.addInterceptor(new HttpHeaderInterceptor.Builder().build());
        //公共参数插值器
        builder.addInterceptor(new CommonParamsInterceptor());

        //缓存使用拦截器
      /*  builder.addInterceptor(sRewriteCacheControlInterceptor);
        builder.addNetworkInterceptor(sRewriteCacheControlInterceptor);
        builder.cache(new Cache(new File(Utils.getContext().getExternalCacheDir() + "/okHttp_cache"), 1024 * 1024 * 100));*/


        // 添加日志拦截器，非debug模式不打印任何日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

    }




    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();//获取请求
            //这里就是说判读我们的网络条件，要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
            if (!NetUtil.isNetworkAvailable(Utils.getContext())) {
                request = request.newBuilder()
                        //这个的话内容有点多啊，大家记住这么写就是只从缓存取，想要了解这个东西我等下在
                        // 给大家写连接吧。大家可以去看下，获取大家去找拦截器资料的时候就可以看到这个方面的东西反正也就是缓存策略。
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.i("CacheInterceptor", "no network");
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable(Utils.getContext())) {
                //这里大家看点开源码看看.header .removeHeader做了什么操作很简答，就是的加字段和减字段的。
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        //这里设置的为0就是说不进行缓存，我们也可以设置缓存时间
                        .header("Cache-Control", "public, max-age=" + 0)
                        .removeHeader("Pragma")
                        .build();
            } else {
                int maxTime = 30 * 24 * 60 * 60;
                return originalResponse.newBuilder()
                        //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    public static RetrofitServiceManager getInstance() {
        if (mRetrofitServiceManager == null) {
            synchronized (RetrofitServiceManager.class) {
                if (mRetrofitServiceManager == null) mRetrofitServiceManager = new RetrofitServiceManager();
            }
        }
        return mRetrofitServiceManager;
    }

    public <T> T creat(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }


}
