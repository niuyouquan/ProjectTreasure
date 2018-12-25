package com.nyq.projecttreasure.utils;

import com.baidu.location.BDLocation;

/**
 * 缓存
 * Created by niuyq on 2017/5/12 0012.
 */
public class AGCache {
    /**
     * 缓存登陆成功后的用户信息
     */
    public static String USER_ACCOUNT;
    public static String USER_PSW;

    /**
     * 当前定位城市编码，城市名称
     */
    public static String CITY_CODE;
    public static String CITY_NAME;
    /**
     * 百度定位Location
     */
    public static BDLocation BD_LOCATION;
}

