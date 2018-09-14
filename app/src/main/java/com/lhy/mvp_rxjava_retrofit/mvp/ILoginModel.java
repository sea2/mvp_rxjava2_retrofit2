package com.lhy.mvp_rxjava_retrofit.mvp;

import java.util.HashMap;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:模拟登陆的操作的接口，实现类为LoginModelImpl.相当于MVP模式中的Model层
 */
public interface ILoginModel {
    void login(HashMap map, LoginModel.LoadDataCallback listener);

    void cancelTasks();
}
