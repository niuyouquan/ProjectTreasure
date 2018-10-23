package com.nyq.projecttreasure.utils;


import java.io.InputStream;
import java.util.Properties;

/**
 * @desc:(客户端配置)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:16/9/8 16:35
 */
public class ReadProperties {
    private static Properties defaultProperty;
    private ReadProperties() {
        //私有化构造方法 隐藏对象
    }
    static {
        init();
    }

    static void init() {
        defaultProperty = new Properties();
        InputStream stream =
                ReadProperties.class.getResourceAsStream("/assets/config.properties");
        try {
            defaultProperty.load(stream);
        } catch (Exception e) {
            LogUtil.info("ReadProperties", e);
        }
        defaultProperty.putAll(System.getProperties());
    }

    /**
     * @param propertyName :对象名称
     * @return 对象值
     * 取得配置文件的对象值
     */
    public static String getPropertyByStr(String propertyName) {
        return String.valueOf(ReadProperties.defaultProperty.get(propertyName));
    }

    /**
     * @param propertyName :对象名称
     * @return 对象值
     * 取得配置文件的对象值
     */
    public static int getPropertyByInt(String propertyName) {
        return Integer.parseInt(ReadProperties.getPropertyByStr(propertyName));
    }

}
