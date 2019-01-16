package com.nyq.projecttreasure.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.okhttp.OkhttpUtil;
import com.nyq.projecttreasure.service.baidaumap.BaiDuLocationService;
import com.nyq.projecttreasure.utils.RootProxyUtil;
import com.rain.library.PhotoPick;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.litepal.LitePal;
import org.xutils.x;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class App extends Application {

    private static Context context;
    private boolean isRoot;//是否手机root
    private boolean isProxy;//是否 设置代理
    private static boolean isDebug = false;
    private static App instance;

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static App getInstance() {
        if (instance == null) {
            synchronized (App.class) {
                if (instance == null) {
                    instance = new App();
                }
            }
        }
        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //设置是否root
        setRoot(RootProxyUtil.isDeviceRooted());
        //设置是否代理
        setProxy(RootProxyUtil.isWifiProxy(getApplicationContext()));
        context = getApplicationContext();
        setIsDebug(false);
        /**
         * 数据库
         */
        LitePal.initialize(this);
        /**
         * xutils3网络框架
         */
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.

        /**
         * 二维码
         */
        ZXingLibrary.initDisplayOpinion(this);

        /**
         * 图片选择
         */
        PhotoPick.init(getApplicationContext(),R.color.colorPrimary);

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        SDKInitializer.initialize(getApplicationContext());

        /**
         * 开启定位服务
         */
        Intent startIntent = new Intent(this, BaiDuLocationService.class);
        startService(startIntent);

        /**
         * zhy的okhttp网络框架
         * 注册OkHttp请求http请求
         * 自己主动取消的错误的 java.net.SocketException: Socket closed
         * 超时的错误是 java.net.SocketTimeoutException
         * 网络出错的错误是java.net.ConnectException: Failed to connect to xxxxx
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new LoggerInterceptor("TAG"))
//				.addInterceptor(new UuidTokenInterceptor(applicationContext))
                //设置连接的超时时间
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                //设置读的超时时间
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                //设置写的超时时间
                .writeTimeout(15000L, TimeUnit.SECONDS)
                .build();
        OkhttpUtil.initClient(okHttpClient);

        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.error_image)
                .showImageOnFail(R.mipmap.error_image)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /**
         * 65535 问题
         */
        MultiDex.install(this);
    }

    public static Context getContextObject() {
        return context;
    }

    public void setIsDebug(boolean isDebug) {
        App.isDebug = isDebug;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }
}
