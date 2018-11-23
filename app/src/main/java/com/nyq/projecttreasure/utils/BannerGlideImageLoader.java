package com.nyq.projecttreasure.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.nyq.projecttreasure.application.App;
import com.youth.banner.loader.ImageLoader;

/**
 * 类名： BannerGlideImageLoader
 * 创建人： niuyq
 * 时间： 2017/10/19 13:15
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
*/
public class BannerGlideImageLoader extends ImageLoader {
    private static BannerGlideImageLoader glideImageLoader;

    /**
     * 单例模式
     * @return
     */
    public static BannerGlideImageLoader init() {
        if (null == glideImageLoader) {
            glideImageLoader = new BannerGlideImageLoader();
        }
        return glideImageLoader;

    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        if (Util.isOnMainThread()) {
            //Glide 加载图片用法
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(path).into(imageView);
        }
    }
}
