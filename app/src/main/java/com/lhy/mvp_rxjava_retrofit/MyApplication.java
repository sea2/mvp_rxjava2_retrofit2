package com.lhy.mvp_rxjava_retrofit;

import android.app.Application;

/**
 * Created by lhy on 2018/9/12.
 */
public class MyApplication extends Application {


    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }


    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


}
