package com.nyq.projecttreasure.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @desc:(屏幕工具类)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:16/9/8 16:35
 */
public class ScreenUtils {

    private ScreenUtils() {
        //私有化构造方法 隐藏对象
    }

    /**
     * 得到手机分辨率的宽度
     *
     * @return
     */
    public static int getEqumentWidth(Activity activity) {
        /* 必须引用android.util.DisplayMetrics */
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return defaultDisplay.getWidth();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return defaultDisplay.getHeight();
    }

    //获取状态栏的高度
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        //使用反射，可能会出现类找不到的异常ClassNotFoundException
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String statusBarHeight = clazz.getField("status_bar_height").get(object).toString();
            int height = Integer.parseInt(statusBarHeight);
            //转化成px返回
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            Log.i("ScreenUtils", e.getMessage());
        }
        return statusHeight;
    }

    //获取当前屏幕截图，包括状态栏
    public static Bitmap getSnapshot(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int screenWidth = getScreenWidth(activity);
        int screenHeight = getScreenHeight(activity);
        Bitmap bp;
        bp = Bitmap.createBitmap(bitmap, 0, 0, screenWidth, screenHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return 返回像素值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @return 返回dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
