package com.lhy.mvp_rxjava_retrofit;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.lhy.mvp_rxjava_retrofit.common.ApiService;
import com.lhy.mvp_rxjava_retrofit.entity.DataTestInfo;
import com.lhy.mvp_rxjava_retrofit.http.BaseObserver;
import com.lhy.mvp_rxjava_retrofit.http.RetrofitServiceManager;
import com.lhy.mvp_rxjava_retrofit.http.RetrofitWithProgressManager;
import com.lhy.mvp_rxjava_retrofit.http.RxUtils;
import com.lhy.mvp_rxjava_retrofit.http.down.RetrofitDownloadListener;
import com.lhy.mvp_rxjava_retrofit.http.entity.BaseResponse;
import com.lhy.mvp_rxjava_retrofit.http.util.Utils;
import com.lhy.mvp_rxjava_retrofit.mvp.MvpActivity;
import com.lhy.mvp_rxjava_retrofit.util.PermissionsUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author Administrator
 */
public class InBaseDealActivity extends BaseActivity implements RetrofitDownloadListener {

    ImageView iv;
    TextView tv;
    NumberProgressBar mNumberProgressBar;
    int completeCount = 0;
    private final int READ_PHONE_STATE_CODE = 2000;

    @Override
    protected void initView() {
        iv = findViewById(R.id.iv);
        tv = findViewById(R.id.tv);
        mNumberProgressBar = findViewById(R.id.number_progress_bar);
        applyPermissions();
    }


    private void applyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] strPermission = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            String[] strNeed = PermissionsUtil.getNeedApplyPermissions(strPermission);
            if (strNeed != null && strNeed.length > 0) {
                ActivityCompat.requestPermissions(this, strNeed, READ_PHONE_STATE_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHONE_STATE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_in_base_deal;
    }

    public void clearData(View view) {
        tv.setText("");
    }



    public void goMvp(View view) {
        startActivity(new Intent(this, MvpActivity.class));
    }

    public void getData(View view) {
        /**
         * 方法2 推荐使用  方便管理
         */

        StringBuilder stringBuilder = new StringBuilder();

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("name", "hehe");

        //直接调用返回返回Result对象
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .getDataType("1")
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<DataTestInfo>(true) {

                    @Override
                    protected void onCompleted(boolean isSuccess, DataTestInfo dataTestInfo, Throwable e) {
                        if (isSuccess) {
                            stringBuilder.append("\n直接调用返回返回Result对象: ");
                            stringBuilder.append(dataTestInfo.getListproject().get(0).toString());
                        }
                        completeCount(stringBuilder);
                    }
                }));

        //直接调用返回全局对象
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .postDataType(map)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleAll())
                .subscribeWith(new BaseObserver<BaseResponse<DataTestInfo>>(true) {
                    @Override
                    protected void onCompleted(boolean isSuccess, BaseResponse<DataTestInfo> dataTestInfoBaseResponse, Throwable e) {
                        if (isSuccess) {
                            stringBuilder.append("\n直接调用返回全局对象: ");
                            stringBuilder.append(dataTestInfoBaseResponse.getResult().getListproject().get(0).toString() + "--");
                        }
                        completeCount(stringBuilder);
                    }
                }));

        //直接调用返回全局String json
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .postDataTypeString(map)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleAllString())
                .subscribeWith(new BaseObserver<String>(true) {
                    @Override
                    protected void onCompleted(boolean isSuccess, String s, Throwable e) {
                        if (isSuccess) {
                            stringBuilder.append("\n直接调用返回全局String json: ");
                            stringBuilder.append(s);
                        }
                        completeCount(stringBuilder);
                    }
                }));


        //放在基类BaseActivity的返回全局对象
        getDataResult(RetrofitServiceManager.getInstance().creat(ApiService.class).postDataType(map), ApiService.request_data_type_code, true, true,
                new BaseObserver<DataTestInfo>() {
                    @Override
                    protected void onCompleted(boolean isSuccess, DataTestInfo dataTestInfo, Throwable e) {
                        if (isSuccess) {
                            stringBuilder.append("\n放在基类BaseActivity的返回全局对象: ");
                            stringBuilder.append(dataTestInfo.getListproject().get(0).toString());
                        }
                        completeCount(stringBuilder);
                    }
                });


        // 放在基类BaseActivity的返回Result对象
        getDataAll(RetrofitServiceManager.getInstance().creat(ApiService.class).postDataType(map), ApiService.request_data_type_code, true, true,
                new BaseObserver<BaseResponse<DataTestInfo>>() {
                    @Override
                    protected void onCompleted(boolean isSuccess, BaseResponse<DataTestInfo> dataTestInfoBaseResponse, Throwable e) {
                        if (isSuccess) {
                            stringBuilder.append("\n放在基类BaseActivity的返回Result对象: ");
                            stringBuilder.append(dataTestInfoBaseResponse.getResult().getListproject().get(0).toString());
                        }
                        completeCount(stringBuilder);
                    }
                });


    }


    /**
     * 线程完成回调，所有全部完成才触发
     *
     * @param stringBuilder
     */
    private synchronized void completeCount(StringBuilder stringBuilder) {
        completeCount++;
        if (completeCount == 5) {
            completeCount = 0;
            tv.setText(stringBuilder);
        }
    }


    /**
     * 下载文件
     *
     * @param view
     */
    public void download(View view) {

        String url_img = "timg?image&quality=80&size=b9999_10000&sec=1536916861981&di=352069dc0b525669b8112ad64f9f4d5d&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-4o3dSag_xI4khGko9WTAnF6hhy%2Flvpics%2Fh%3D800%2Fsign%3Db49dc48f8718367ab28972dd1e728b68%2F9922720e0cf3d7ca7f0736d0f31fbe096a63a9a6.jpg";


        addSubscribe(RetrofitWithProgressManager
                .getInstance(this, "https://timgsa.baidu.com/")
                .creat(ApiService.class)
                .downLoad(url_img)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        String pathFile = Utils.getContext().getExternalCacheDir().getAbsolutePath() + "/down_img/";
                        String pathName = "img.jpg";
                        writeFile(responseBody.byteStream(), pathFile, pathName);
                        return pathFile.concat(pathName);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<String>) str -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(str);
                    iv.setImageBitmap(bitmap);
                }));
    }


    /**
     * 将输入流写入文件
     */
    private void writeFile(InputStream inputString, String pathDirectorStr, String fileName) {
        try {
            //创建目录
            File fileDirector = new File(pathDirectorStr);
            if (!fileDirector.exists()) {
                fileDirector.mkdirs();
            }

            //创建文件
            File file = new File(pathDirectorStr.concat(fileName));
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream fos = null;

            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }


    @Override
    public void onStartDownload() {

    }

    @Override
    public void onProgress(int progress) {

        runOnUiThread(() -> {
            mNumberProgressBar.setVisibility(View.VISIBLE);
            mNumberProgressBar.setProgress(progress);
            if (progress == 100) {
                mNumberProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onFinishDownload() {

    }

    @Override
    public void onFail(String errorInfo) {

    }
}
