package com.nyq.projecttreasure.activitys.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.next.easynavigition.view.EasyNavigitionBar;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.start.GestureLoginActivity;
import com.nyq.projecttreasure.activitys.start.LoginActivity;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.fragments.FindFragment;
import com.nyq.projecttreasure.fragments.HomeFragment;
import com.nyq.projecttreasure.fragments.MeFragment;
import com.nyq.projecttreasure.fragments.MsgFragment;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.ActivityManager;
import com.nyq.projecttreasure.utils.SPUtils;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigitionBar)
    EasyNavigitionBar navigitionBar;

    private long exitTime;
    private String[] tabText = {"首页", "消息", "发现", "我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.index, R.mipmap.find, R.mipmap.message, R.mipmap.me};
    //选中时icon
    private int[] selectIcon = {R.mipmap.index1, R.mipmap.find1, R.mipmap.message1, R.mipmap.me1};

    private List<Fragment> fragments = new ArrayList<>();
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragments.add(HomeFragment.newInstance());
        fragments.add(MsgFragment.newInstance());
        fragments.add(FindFragment.newInstance());
        fragments.add(MeFragment.newInstance());


        View view = LayoutInflater.from(this).inflate(R.layout.custom_add_view, null);

        navigitionBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .canScroll(true)
                .addAsFragment(false)
                .mode(EasyNavigitionBar.MODE_ADD_VIEW)
                .addCustomView(view)
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigitionBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if (position == 4) {
                            if (StringHelper.isBlank(AGCache.USER_ACCOUNT) || StringHelper.isBlank(AGCache.USER_PSW)){
                                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                boolean isCheckeds = (boolean) SPUtils.get(MainActivity.this,"isChecked",false);
                                if (isCheckeds) {
                                    Intent intent = new Intent(MainActivity.this, GestureLoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                //return true则拦截事件、不进行页面切换
                                return true;
                            }
                        } else if (position == 2) {
                            //＋ 旋转动画
                            if (flag) {
                                navigitionBar.getCustomAddView().animate().rotation(45).setDuration(400);
                            } else {
                                navigitionBar.getCustomAddView().animate().rotation(0).setDuration(400);
                            }
                            flag = !flag;
                            navigitionBar.setMsgPointCount(0,20); //红点未读数
                        }
                        return false;
                    }
                })
                .build();
    }

    public EasyNavigitionBar getNavigitionBar() {
        return navigitionBar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 1.使用getSupportFragmentManager().getFragments()获取到当前Activity中添加的Fragment集合
         * 2.遍历Fragment集合，手动调用在当前Activity中的Fragment中的onActivityResult()方法。
         */
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime = System.currentTimeMillis();
            if (curTime - exitTime > 2000) {
                ToastUtil.refreshToast(MainActivity.this, "再按一次退出应用", 1);
                exitTime = curTime;
            } else {
                ActivityManager.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
