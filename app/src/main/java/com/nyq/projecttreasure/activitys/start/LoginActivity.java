package com.nyq.projecttreasure.activitys.start;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.main.MainActivity;
import com.nyq.projecttreasure.application.ParseUserData;
import com.nyq.projecttreasure.application.ServerData_Pref;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.dialog.LoadingAnimationDialog;
import com.nyq.projecttreasure.interfaces.OnDownAPKListenter;
import com.nyq.projecttreasure.models.LoginBean;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.ApkUpdateManager;
import com.nyq.projecttreasure.utils.Constant;
import com.nyq.projecttreasure.utils.CurrentVersion;
import com.nyq.projecttreasure.utils.JsonUtils;
import com.nyq.projecttreasure.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_account_et)
    EditText loginAccountEt;
    @BindView(R.id.login_account_ll)
    LinearLayout loginAccountLl;
    @BindView(R.id.login_view_line)
    View loginViewLine;
    @BindView(R.id.login_password_et)
    EditText loginPasswordEt;
    @BindView(R.id.login_password_ll)
    LinearLayout loginPasswordLl;
    @BindView(R.id.login_view_lines)
    View loginViewLines;
    @BindView(R.id.login_rember_pwd_cb)
    CheckBox loginRemberPwdCb;
    @BindView(R.id.login_auto_login_cb)
    CheckBox loginAutoLoginCb;
    @BindView(R.id.login_login_btn)
    Button loginLoginBtn;

    private Dialog loadingAnimationDialog;
    private static String versionName;
    private ApkUpdateManager mUpdateManager;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean rememberPSW, autoLogin;
    private String login_name, login_psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**全屏设置，隐藏窗口所有装饰**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loging);
        ButterKnife.bind(this);
        mUpdateManager = new ApkUpdateManager(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            /**
             * 检查更新
             */
            jianChaGengXin();
        } else {
            if (PermissionsUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                /**
                 * 检查更新
                 */
                jianChaGengXin();
            } else {
                PermissionsUtil.requestPermission(this, new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permissions) {
                        /**
                         * 检查更新
                         */
                        jianChaGengXin();
                    }
                    @Override
                    public void permissionDenied(@NonNull String[] permissions) {
                        ToastUtil.refreshToast(LoginActivity.this, "无此权限可能会导致系统更新后不能安装", 0);
                        //TODO:无权限就不更新了
                    }
                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        }
    }


    private void next() {
        initView();
        initEvent();
        initSlide();
    }


    public void jianChaGengXin(){
        //获取版本号
        try {
            versionName = CurrentVersion.getVersionName(LoginActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            versionName = "1.0";
        }

        /**
         * 有更新的版本，选择更新
         */
        String url = "http://m.shouji.360tpcdn.com/161021/5c9660adb69c622b7021f407b7e11afe/com.lzjs.happoint_1.apk";
        int fileSize = 10391389;
        String version = "1.0";
        String descs = "更新、爱上对方就是快递费";
        mUpdateManager.updateShowDialog(url, fileSize, version, descs);
//        next();
        mUpdateManager.setOnDownAPKListenter(new OnDownAPKListenter() {
            @Override
            public void onCancel() {
                next();
            }
        });
    }

    /**
     * 初始化控件
     */
    public void initView() {
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        editor = sp.edit();
        //不能输入空格
        setEditTextInhibitInputSpace(loginAccountEt);
        setEditTextInhibitInputSpace(loginPasswordEt);

        loginAccountEt.addTextChangedListener(textWatcherListener(loginAccountEt));
        loginPasswordEt.addTextChangedListener(textWatcherListener(loginPasswordEt));
        rememberPSW = sp.getBoolean("rememberPSW", false);
        if (rememberPSW) {
            loginAccountEt.setText(sp.getString("account", ""));
            loginPasswordEt.setText(sp.getString("password", ""));
            loginRemberPwdCb.setChecked(true);
        } else {
            loginAccountEt.setText("");
            loginPasswordEt.setText("");
        }
        loginPasswordEt.requestFocus();
        loginAccountEt.requestFocus();  //使光标在最后
    }

    /**
     *
     */
    private void initEvent() {
        loginLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAnimationDialog =
                        LoadingAnimationDialog.show(LoginActivity.this, "", "正在登录,请稍候...", true);
                loadingAnimationDialog.show();
                mHandler.sendEmptyMessage(Constant.MSG_LOGINING_SHOW);
            }
        });
        loginAutoLoginCb.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (!loginRemberPwdCb.isChecked()) {
                                loginRemberPwdCb.setChecked(true);
                            }
                        }
                    }
                });
        loginRemberPwdCb.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (loginAutoLoginCb.isChecked()) {
                                ToastUtil.refreshToast(LoginActivity.this, "自动登陆状态下必须选择记住密码", 1);
                                loginRemberPwdCb.setChecked(true);
                            }
                        } else {
                            if (loginAutoLoginCb.isChecked()) {
                                ToastUtil.refreshToast(LoginActivity.this, "自动登陆状态下必须选择记住密码", 1);
                                loginRemberPwdCb.setChecked(true);
                            }
                        }
                    }
                });
    }

    private void initSlide() {

        autoLogin = sp.getBoolean("autoLogin", false);
        if (autoLogin) {
            loginAutoLoginCb.setChecked(true);
            mHandler.sendEmptyMessage(Constant.MSG_LOGINING_SHOW);
        }
    }

    private CharSequence temp;
    private int editStart;
    private int editEnd;
    private boolean isFrist = true;
    /**
     * 账号，密码输入框监听
     */
    public TextWatcher textWatcherListener(final View view) {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (view.getId()) {
                    case R.id.login_password_et:
                        editStart = loginPasswordEt.getSelectionStart();
                        editEnd = loginPasswordEt.getSelectionEnd();
                        if (temp.length() > 10) {
                            if (isFrist) {
                                ToastUtil.showToast(LoginActivity.this, "你输入的字数已经超过了限制！", 0);
                                isFrist = false;
                            }
                            s.delete(editStart - 1, editEnd);
                            int tempSelection = editStart;
                            loginPasswordEt.setText(s);
                            loginPasswordEt.setSelection(tempSelection);
                        }
                        break;
                    case R.id.login_account_et:
                        editStart = loginAccountEt.getSelectionStart();
                        editEnd = loginAccountEt.getSelectionEnd();
                        if (temp.length() > 10) {
                            if (isFrist) {
                                ToastUtil.showToast(LoginActivity.this, "你输入的字数已经超过了限制！", 0);
                                isFrist = false;
                            }
                            s.delete(editStart - 1, editEnd);
                            int tempSelection = editStart;
                            loginAccountEt.setText(s);
                            loginAccountEt.setSelection(tempSelection);
                        }
                        break;
                    default:
                        break;
                }


            }
        };
        return textWatcher;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_LOGINING_SHOW:
                    if (isNull()) {
                        return;
                    }
                    //连接服务器--http请求
                    httpLogin();
                    break;
                case Constant.MSG_PROGRESSDIALOG_DISMISS:
                    if (loadingAnimationDialog != null) {
                        loadingAnimationDialog.dismiss();
                    }
                    break;
                case 1:
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                    break;
                default:
                    break;
            }
        }
    };

    public void httpLogin() {
        if (200 == Constant.HTTP_SUCCESS_CODE){
            if (login_name.equals("niuyq") && login_psw.equals("123456789")){
                LoginService();
            }else {
                ToastUtil.refreshToast(LoginActivity.this, "用户名或密码不正确", 1);
            }
        }
//        AgriClient.getInstance().Login(login_name, login_psw,
//                new AgriCallback<LoginBean>() {
//                    @Override
//                    public void onSuccess(LoginBean response, int id) {
//                        if (response.getCode() == Constants.HTTP_SUCCESS_CODE) {
//                            LoginService(response);
//                            finish();
//                        } else {
//                            mHandler.sendEmptyMessage(Constants.MSG_PROGRESSDIALOG_DISMISS);
//                            mHandler.sendEmptyMessage(0);
//                            ToastUtil.refreshToast(LoginActivity.this, (String) response.getMsg(), 1);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e, int id) {
//                        ToastUtil.refreshToast(LoginActivity.this, "数据服务器连接异常", 1);
//                        mHandler.sendEmptyMessage(Constants.MSG_PROGRESSDIALOG_DISMISS);
//                        mHandler.sendEmptyMessage(0);
//                        return;
//                    }
//                });
    }

    private void LoginService() {
        //写入缓存
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        AGCache.USER_ACCOUNT = login_name;
        AGCache.USER_PSW = login_psw;
//        //tcp请求箱子
//        Map<String, Object> map = new HashMap<>();
//        map.put("loginAccount", login_name);
//        map.put("loginPwd", login_psw);
//        IMClient.sendData(AGCache.ID, AGCache.SYSTEMID, Constants.LOGIN_CMD, map);
//        if (timer != null) {
//            timer.cancel();
//        }

//        ServerData_Pref mServerData_Pref = new ServerData_Pref(getApplicationContext());
//        mServerData_Pref.clearAppData();
//        LoginBean appData = new ParseUserData().parseUserLoginAppData(JsonUtils.objectToString(resInfo.getData()));
//        mServerData_Pref.saveAppData(appData);
        if (loginRemberPwdCb.isChecked()) {
            editor.putBoolean("rememberPSW", true);
            editor.putString("account", login_name);
            editor.putString("password", login_psw);
        } else {
            editor.putBoolean("rememberPSW", false);
        }
        if (loginAutoLoginCb.isChecked()) {
            editor.putBoolean("autoLogin", true);
        } else {
            editor.putBoolean("autoLogin", false);
        }
        editor.commit();
        mHandler.sendEmptyMessage(Constant.MSG_PROGRESSDIALOG_DISMISS);
    }

    private boolean isNull() {
        login_name = loginAccountEt.getText().toString();
        login_psw = loginPasswordEt.getText().toString();
        if (TextUtils.isEmpty(login_name)) {
            ToastUtil.refreshToast(LoginActivity.this, "请输入账号", 1);
            if (loadingAnimationDialog != null) {
                loadingAnimationDialog.dismiss();
            }
            return true;
        } else if (TextUtils.isEmpty(login_psw)) {
            ToastUtil.refreshToast(LoginActivity.this, "请输入密码", 1);
            if (loadingAnimationDialog != null) {
                loadingAnimationDialog.dismiss();
            }
            return true;
        }
        return false;
    }
}
