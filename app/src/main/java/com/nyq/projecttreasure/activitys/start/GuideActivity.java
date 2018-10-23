package com.nyq.projecttreasure.activitys.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.main.MainActivity;
import com.nyq.projecttreasure.adapter.GuidePagerAdapter;
import com.nyq.projecttreasure.application.ParseUserData;
import com.nyq.projecttreasure.application.ServerData_Pref;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.models.LoginBean;
import com.nyq.projecttreasure.utils.Constant;
import com.nyq.projecttreasure.utils.CurrentVersion;
import com.nyq.projecttreasure.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:(第一次启动页面)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:2017/2/14 11:09
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private int[] imageIdArray;//图片资源的数组
    /**
     * 引导圆点
     */
    private ImageView[] points;
    /**
     * 记录当前选中位置
     */
    private int currentIndex;
    //最后一页的按钮
    private ImageButton ibStart;
    String from = "";

    private LoginBean appData;
    private ServerData_Pref mServerDataPref;
    private ViewPager vp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppData();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        from = this.getIntent().getStringExtra("FROM");
        setContentView(R.layout.activity_guide);
        ibStart = (ImageButton) findViewById(R.id.guide_ib_start);
        ibStart.setOnClickListener(v -> {
            if (StringHelper.isNotBlank(from) && "SETTING".equals(from)) {
                activity.finish();
            } else {
                if (appData == null) {
                    appData = new ParseUserData().initVisitor();
                }
                mServerDataPref.saveAppData(appData);
                // 设置已经引导
                setGuided();
                goLogin();
            }

        });
        //加载ViewPager
        initViewPager();
    }

    private void initAppData() {
        if (mServerDataPref == null) {
            mServerDataPref = new ServerData_Pref(this);
        }
        appData = mServerDataPref.getAppData();
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //实例化图片资源
        imageIdArray = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};
        List<View> viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0; i < len; i++) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);
            //将ImageView加入到集合中
            viewList.add(imageView);
        }
        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePagerAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);

        // 初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

        points = new ImageView[imageIdArray.length];

        // 循环取得小点图片
        for (int i = 0; i < imageIdArray.length; i++) {
//            // 得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //页面滑动
    }

    /**
     * 滑动后的监听
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //判断是否是最后一页，若是则显示按钮
        setCurDot(position);
        showStartTV(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //滑动状态改变
    }

    private void goLogin() {
        // 跳转 登录页
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = activity.getSharedPreferences(Constant.SAVE_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putString(Constant.APP_VERSION_CODE, CurrentVersion.getVersionName(activity));
        // 提交修改
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 展示开启页面
     *
     * @param position
     */
    private void showStartTV(int position) {
        if (position == imageIdArray.length - 1) {
            ibStart.setVisibility(View.VISIBLE);
        } else {
            ibStart.setVisibility(View.GONE);
        }
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= imageIdArray.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > imageIdArray.length - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        showStartTV(position);
        setCurView(position);
        setCurDot(position);

    }
}
