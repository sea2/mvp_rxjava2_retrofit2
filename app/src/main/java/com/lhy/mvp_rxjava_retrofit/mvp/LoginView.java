package com.lhy.mvp_rxjava_retrofit.mvp;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:登陆View的接口，实现类也就是登陆的activity
 */
public interface LoginView<T> {
    void showProgress();

    void setData(T t);

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();
}
