package com.nyq.projecttreasure.activitys.mymessage;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.selectphoto.GlideImageLoader;
import com.nyq.projecttreasure.utils.ToastUtil;
import com.rain.library.controller.PhotoPickConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyMessageActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.menu)
    TextView menu;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        ButterKnife.bind(this);
        setStatusBarColor(R.color.colorPrimary);
        initView();
    }

    public void initView() {
        back.setVisibility(View.VISIBLE);
        title.setText("个人信息");
        menu.setText("保存");

        back.setOnClickListener(this);
        menu.setOnClickListener(this);
        headImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
                break;
            case R.id.head_img:
                new PhotoPickConfig
                        .Builder(activity)
                        .imageLoader(GlideImageLoader.init())                //图片加载方式，支持任意第三方图片加载库
                        .spanCount(PhotoPickConfig.GRID_SPAN_COUNT)         //相册列表每列个数，默认为3
                        .pickMode(PhotoPickConfig.MODE_PICK_SINGLE)           //设置照片选择模式为单选，默认为单选
                        .maxPickSize(PhotoPickConfig.DEFAULT_CHOOSE_SIZE)   //多选时可以选择的图片数量，默认为1张
                        .showCamera(true)           //是否展示相机icon，默认展示
                        .clipPhoto(true)            //是否开启裁剪照片功能，默认关闭
                        .clipCircle(true)          //是否裁剪方式为圆形，默认为矩形
                        .build();
                break;
            case R.id.menu:
                ToastUtil.refreshToast(activity,"保存",Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PhotoPickConfig.PICK_SINGLE_REQUEST_CODE:      //单选不裁剪
                String path = data.getStringExtra(PhotoPickConfig.EXTRA_SINGLE_PHOTO);
                Log.e("单选", path);
                break;
            case PhotoPickConfig.PICK_CLIP_REQUEST_CODE:    //裁剪
                Uri resultUri = null;
                //判读版本是否在7.0以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    resultUri = FileProvider.getUriForFile(mContext, "com.nyq.projecttreasure.provider", new File(data.getStringExtra(PhotoPickConfig.EXTRA_CLIP_PHOTO)));
                } else {
                    resultUri = Uri.parse(data.getStringExtra(PhotoPickConfig.EXTRA_CLIP_PHOTO));
                }
                Glide.with(getApplicationContext()).
                        load(resultUri)
                        .into(headImg);
                break;
        }
    }

    //为状态栏着色
    public void setStatusBarColor(int colorRes) {
        toolbar.setBackgroundResource(colorRes);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
    }
}
