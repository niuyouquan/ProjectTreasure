package com.nyq.projecttreasure.utils;

/**
 * @desc:(常量定义）
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:16/9/8 15:37
 */
public class Constant {

    public static final String APP_NAME = "xmb.apk";//名字
    public static final String APP_IMG_PATH = "xmb/";//图片路径
    public static final String APP_NAME_CN = "项目宝";//名字
    public static final String APP_SIGNATURE = "【项目宝】";
    public static final String OPTIONAL = "0";//自愿更新
    public static final String FORCIBLEY = "1";//强制更新

    public static final String PRODUCT_TYPE = "android";//产品类型
    public static final String RESPONSE_OTHER = "2";//请求结果-成功
    public static final String RESPONSE_SUCCESS = "0";//请求结果-成功
    public static final String RESPONSE_ERROR = "1";//请求结果-失败
    public static final String RESPONSE_INVALID = "9";//token失效
    public static final String SYS_TAG = "XMB";
    public static final String SYS_NET_ERROY = "网络连接错误";
    public static final String CHAR_SET = "UTF-8";    //客户端通讯字符集

    /**
     * 进度条显示
     */
    public final static int MSG_LOGINING_SHOW = 0X123;
    /**
     * 进度条消失
     */
    public final static int MSG_PROGRESSDIALOG_DISMISS = 0X122;
    /**
     * http请求码
     */
    public static final int HTTP_SUCCESS_CODE = 200; //成功

    public static final String SAVE_SETTING = "XMB_SETTING";
    public static final String APP_VERSION_CODE = "APP_VERSION_CODE";
    public static final String APP_IS_FIRST_RUN = "APP_IS_FIRST_RUN";

    public static final String LOGOUT_RECEIVER_ACTION = "com.nyq.projecttreasure.logout";

    public static final String VISTOR = "xmb_youke";
    public static final String ACCOUNT = "";
    public static final String VISTOR_TOKEN = "4HFhcmjkgsyt174m";


    public static final String serverUrl = ReadProperties.getPropertyByStr("server.url");
    public static final String pageSize = ReadProperties.getPropertyByStr("list.pagesize");

    /**
     * HTTPS请求port
     */
    public static final String PORT = ":9999/";
    /**
     * 登陆
     */
    public static final String LOGIN_URL ="a/login";

    /**
     * 通知
     */
    public static final String ACTION_CLOSE = "com.nyq.projecttreasure.notification.close";

    /**
     * 定位广播
     */
    public static final String ACTION_LOCATION = "com.nyq.projecttreasure.location";
}
