package com.lhy.mvp_rxjava_retrofit.http.cache;

import android.database.sqlite.SQLiteDatabase;

import com.lhy.mvp_rxjava_retrofit.http.util.Utils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * 数据缓存
 * 数据库工具类-geendao运用
 * Created by WZG on 2016/10/25.
 */

public class CacheResultDbUtil {
    private static CacheResultDbUtil db;
    private final static String dbName = "http_cache_db";
    private DaoMaster.DevOpenHelper openHelper;


    public CacheResultDbUtil() {
        openHelper = new DaoMaster.DevOpenHelper(Utils.getContext(), dbName);
    }


    /**
     * 获取单例
     * @return
     */
    public static CacheResultDbUtil getInstance() {
        if (db == null) {
            synchronized (CacheResultDbUtil.class) {
                if (db == null) {
                    db = new CacheResultDbUtil();
                }
            }
        }
        return db;
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(Utils.getContext(), dbName);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(Utils.getContext(), dbName);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void saveCookie(CacheResult info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CacheResultDao downInfoDao = daoSession.getCacheResultDao();
        downInfoDao.insert(info);
    }

    public void updateCookie(CacheResult info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CacheResultDao downInfoDao = daoSession.getCacheResultDao();
        downInfoDao.update(info);
    }

    public void deleteCookie(CacheResult info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CacheResultDao downInfoDao = daoSession.getCacheResultDao();
        downInfoDao.delete(info);
    }


    public CacheResult queryCookieBy(String url) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CacheResultDao downInfoDao = daoSession.getCacheResultDao();
        QueryBuilder<CacheResult> qb = downInfoDao.queryBuilder();
        qb.where(CacheResultDao.Properties.Url.eq(url));
        List<CacheResult> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<CacheResult> queryCookieAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CacheResultDao downInfoDao = daoSession.getCacheResultDao();
        QueryBuilder<CacheResult> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
