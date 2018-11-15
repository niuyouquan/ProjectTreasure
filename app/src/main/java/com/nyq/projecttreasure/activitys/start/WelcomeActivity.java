package com.nyq.projecttreasure.activitys.start;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.main.MainActivity;
import com.nyq.projecttreasure.application.App;
import com.nyq.projecttreasure.application.ParseUserData;
import com.nyq.projecttreasure.application.ServerData_Pref;
import com.nyq.projecttreasure.dialog.CustomDialog;
import com.nyq.projecttreasure.models.LoginBean;
import com.nyq.projecttreasure.utils.Constant;
import com.nyq.projecttreasure.utils.CurrentVersion;
import com.nyq.projecttreasure.utils.StringHelper;
import java.util.List;

import static com.nyq.projecttreasure.utils.Constant.SAVE_SETTING;

/**
 * splash界面
 */
public class WelcomeActivity extends Activity {
    private String appVersionCode = "";
    private static final int GO_GUIDE = 1001;
    private static final int GO_MAIN = 1002;

    View rootView;
    private LoginBean appData;
    private ServerData_Pref mServerDataPref;
    private App app;
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    goGuide();
                    break;
                case GO_MAIN:
                    goMain();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private Handler shandler = new Handler();

    private void initAppData() {
        if (mServerDataPref == null) {
            mServerDataPref = new ServerData_Pref(this);
        }
        appData = mServerDataPref.getAppData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        initAppData();
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_splash, null);
        setContentView(rootView);
        //这是一个 Handler 里面的逻辑是从 Splash 界面跳转到 Main 界面，这里的逻辑每个公司基本上一致
        shandler.postDelayed(() -> {
            // 使用SharedPreferences来记录程序的使用次数
            SharedPreferences preferences = getSharedPreferences(SAVE_SETTING, Context.MODE_PRIVATE);
            // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
            appVersionCode = preferences.getString(Constant.APP_VERSION_CODE, "");
            // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
            // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
            boolean isFirstRun = preferences.getBoolean(Constant.APP_IS_FIRST_RUN, true);
            // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
            if (StringHelper.isBlank(appVersionCode) || (StringHelper.isNotBlank(appVersionCode) && !appVersionCode.equals(CurrentVersion.getVersionName(WelcomeActivity.this)))) {
                if (isFirstRun) {
                    if (app.isRoot()) {
                        showRootDialog(GO_GUIDE);
                    } else {
                        mHandler.sendEmptyMessage(GO_GUIDE);
                    }
                } else {
                    mHandler.sendEmptyMessage(GO_GUIDE);
                }
            } else {
                if (appData == null) {
                    appData = new ParseUserData().initVisitor();
                }
                mServerDataPref.saveAppData(appData);
                if (isFirstRun) {
                    if (app.isRoot()) {
                        showRootDialog(GO_MAIN);
                    } else {
                        mHandler.sendEmptyMessage(GO_MAIN);
                    }
                } else {
                    mHandler.sendEmptyMessage(GO_MAIN);
                }

            }
        }, 3000);
//        if (appData != null && appData.getUserData() != null
//                && appData.getUserData().getUserId() != null
//                && !"xmb_youke".equals(appData.getUserData().getUserId())
//                && !isServiceWork(this, "com.gsww.gsrhc.jkgs.service.UpdateVisitCardService")) {
//            //开启后台同步数据服务
//            Intent service = new Intent(this, UpdateVisitCardService.class);
//            service.putExtra("USER_ID", appData.getUserData().getUserId());
//            startService(service);
//        }
    }

    private void goGuide() {
        Intent intent = new Intent(this, GuideActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showRootDialog(int go) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this)
                .setImgSrc(0)
                .setMessage("您的手机已经root，继续使用会有数据泄露风险，是否继续使用？")
                .setCancel()
                .setPositiveButton("确定", R.color.ml_gray, (dialog, which) -> {
                    dialog.dismiss();
                    if (app.isProxy()) {
                        showWifiProxy(go);
                    } else {
                        SharedPreferences preferences = getSharedPreferences(SAVE_SETTING, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Constant.APP_IS_FIRST_RUN, false);
                        editor.commit();
                        mHandler.sendEmptyMessage(go);
                    }
                })
                .setNegativeButton("退出", R.color.actionsheet_blue, (dialog, which) -> {
                    System.exit(0);
                    dialog.dismiss();
                });
        builder.create().show();
        builder.create().setCanceledOnTouchOutside(false);
    }

    private void showWifiProxy(int go) {

        CustomDialog.Builder builder = new CustomDialog.Builder(this)
                .setImgSrc(0)
                .setMessage("当前正在使用代理上网，是否退出应用!")
                .setCancel()
                .setPositiveButton("取消", R.color.ml_gray, (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences preferences = getSharedPreferences(SAVE_SETTING, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(Constant.APP_IS_FIRST_RUN, false);
                    editor.commit();
                    mHandler.sendEmptyMessage(go);
                })
                .setNegativeButton("退出", R.color.actionsheet_blue, (dialog, which) -> {
                    System.exit(0);
                    dialog.dismiss();
                });
        builder.create().show();
        builder.create().setCanceledOnTouchOutside(false);
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
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.isEmpty()) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
