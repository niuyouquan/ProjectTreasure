package com.nyq.projecttreasure.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.application.App;
import com.nyq.projecttreasure.application.ParseUserData;
import com.nyq.projecttreasure.application.ServerData_Pref;
import com.nyq.projecttreasure.dialog.CustomDialog;
import com.nyq.projecttreasure.models.LoginBean;
import com.nyq.projecttreasure.utils.ActivityManager;
import com.nyq.projecttreasure.utils.Constant;
import com.nyq.projecttreasure.utils.HandlerUtils;
import com.nyq.projecttreasure.utils.LogUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * @desc:(BaseActivity)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:16/9/8 10:12
 */
public class BaseActivity extends AppCompatActivity implements HandlerUtils.OnReceiveMessageListener {

    protected final String TAG = this.getClass().getSimpleName();
    protected Activity activity;
    protected Context mContext;
    private Fragment mFragment;
    protected HandlerUtils.HandlerHolder handlerHolder;

    //其他设备登录接收器
    private OtherEquipmentLoginReceiver receiver;
    private LoginBean appData;
    private ServerData_Pref mServerDataPref;

    /**
     * 子类中接受不同的消息去进行相应处理
     *
     * @param msg
     */
    @Override
    public void handlerMessage(Message msg) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        initIntent(getIntent());
        //判断SDK版本是否大于等于19，大于就让他显示，小于就要隐藏，不然低版本会多出来一个
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // 5.0系统以上才开启沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        mContext = this;
        activity = this;
        initReceiver();
        handlerHolder = new HandlerUtils.HandlerHolder(this);
    }

    /**
     * 初始化参数传递
     *
     * @param intent
     */
    protected void initIntent(Intent intent) {
        //初始化参数传递
    }

    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }


    protected void initReceiver() {
        //初始化接收广播
        receiver = new OtherEquipmentLoginReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.LOGOUT_RECEIVER_ACTION);
        registerReceiver(receiver, intentFilter);

    }

    protected void quit() {
        try {
            ActivityManager.getInstance().finishAll();
        } catch (Exception e) {
            LogUtil.info(TAG, e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
        //解除广播
        unregisterReceiver(receiver);
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //为状态栏着色
    public void setStatusBarColor(int colorRes) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
    }

    /**
     * 在其他设备登录
     */
    public class OtherEquipmentLoginReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (Constant.LOGOUT_RECEIVER_ACTION.equals(intent.getAction())) {
                otherEquipmentLogin(intent.getStringExtra("logoutMsg"));
            }
        }
    }

    CustomDialog dialog = null;

    private void otherEquipmentLogin(String msg) {

        if (ActivityManager.getInstance().getCurrentActivity() == this) {
            if (dialog == null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(activity)
                        .setMessage(msg)
                        .setCancel()
                        .setNegativeButton("我知道了", R.color.actionsheet_blue, (dialog, which) -> {
                            initAppData();
                            //清空栈顶的页面
                            ActivityManager.getInstance().clearTopActivityAll("MainActivity");
//                            //账号登出时需调用此接口，调用之后不再发送账号相关内容
//                            MobclickAgent.onProfileSignOff();
//                            XGPushManager.registerPush(getApplicationContext(), "*");
                            //清理缓存
//                            SPUtils.put(activity, "LTime", System.currentTimeMillis());
                            mServerDataPref.clearAppData();
                            appData = new ParseUserData().initVisitor();
                            mServerDataPref.saveAppData(appData);
//                            Intent intent = new Intent(this, LoginActivity.class);
//                            startActivity(intent);
                            if (!ActivityManager.getInstance().isForeground(this, "MainActivity")) {
                                finish();
                            }
                            dialog.dismiss();
                        });
                builder.create().setCanceledOnTouchOutside(false);
                dialog = builder.create();
                dialog.show();
            } else {
                if (!isFinishing() && !dialog.isShowing()) {
                    dialog.show();
                }
            }
        }
    }

    private void initAppData() {
        if (mServerDataPref == null) {
            mServerDataPref = new ServerData_Pref(App.getContextObject());
        }
        appData = mServerDataPref.getAppData();
    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (" ".equals(source)) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

}
