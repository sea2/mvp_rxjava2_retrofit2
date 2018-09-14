package com.lhy.mvp_rxjava_retrofit.mvp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by lhy on 2017/12/13.
 */

public abstract class BaseMvpActivity<V, P extends BasePresenter<V>>
        extends Activity {

    protected P mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attach((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    public abstract P createPresenter();


    protected void showLoading(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(content);
            mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        mProgressDialog.setMessage(content);
        mProgressDialog.show();
    }

    protected void showLoading() {
        showLoading("正在加载中...");
    }

    protected void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


}