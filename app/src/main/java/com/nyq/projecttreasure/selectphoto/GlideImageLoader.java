package com.nyq.projecttreasure.selectphoto;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nyq.projecttreasure.R;
import com.rain.library.loader.ImageLoader;

/**
 * 类名： GlideImageLoader
 * 创建人： niuyq
 * 时间： 2017/10/19 13:15
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
*/
public class GlideImageLoader implements ImageLoader {

    private final static String TAG = "GlideImageLoader";
    private static GlideImageLoader glideImageLoader;
    /**
     * 单例模式
     * @return
     */
    public static GlideImageLoader init() {
        if (null == glideImageLoader) {
            glideImageLoader = new GlideImageLoader();
        }
        return glideImageLoader;

    }
    @Override
    public void displayImage(Context context, String path, ImageView imageView, boolean resize) {
        DrawableRequestBuilder builder = null;
        builder = Glide.with(context).load(path);
        if (resize) {
            builder = builder.centerCrop();
            builder.crossFade()
                    .error(context.getResources().getDrawable(R.mipmap.error_image))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }
    }



    @Override
    public void clearMemoryCache() {

    }
}
