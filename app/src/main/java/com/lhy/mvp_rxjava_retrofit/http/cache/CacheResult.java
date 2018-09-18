package com.lhy.mvp_rxjava_retrofit.http.cache;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * post請求緩存数据
 * Created by WZG on 2016/10/26.
 */
@Entity
public class CacheResult {
    @Id(autoincrement = true)
    private Long id;
    /*url*/
    private String url;
    /*返回结果*/
    private String result;
    /*时间*/
    private long time;




    @Generated(hash = 1780925602)
    public CacheResult(Long id, String url, String result, long time) {
        this.id = id;
        this.url = url;
        this.result = result;
        this.time = time;
    }



    @Generated(hash = 340389037)
    public CacheResult() {
    }

    public <T> CacheResult(Long id, String url, T t, long time) {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
