package com.lhy.mvp_rxjava_retrofit.mvp;

import android.os.Bundle;
import android.widget.TextView;

import com.lhy.mvp_rxjava_retrofit.R;
import com.lhy.mvp_rxjava_retrofit.entity.DataTestInfo;

import java.util.HashMap;

public class MvpActivity extends BaseMvpActivity<LoginView, LoginPresenter> implements LoginView<DataTestInfo> {

    private android.widget.TextView tvtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        this.tvtext = (TextView) findViewById(R.id.tv_text);

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "1");
        mPresenter.validateCredentials(map);
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }


    @Override
    public void showProgress() {
        showLoading();
    }

    @Override
    public void setData(DataTestInfo dataTestInfo) {
        tvtext.setText(dataTestInfo.getListproject().get(0).toString());
    }


    @Override
    public void hideProgress() {
        dismissLoading();
    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void navigateToHome() {

    }

}
