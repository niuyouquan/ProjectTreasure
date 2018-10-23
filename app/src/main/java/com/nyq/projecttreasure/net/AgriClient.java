package com.nyq.projecttreasure.net;

import android.util.Log;

import com.google.gson.Gson;
import com.nyq.projecttreasure.models.LoginBean;
import com.nyq.projecttreasure.utils.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static android.content.ContentValues.TAG;

/**
 * Created by Liu_xg on 2017/3/2.
 */

public class AgriClient {
    //单列模式
    private static AgriClient mAgriClient;

    private Gson mGson;

    public AgriClient() {
        mGson = new Gson();
    }

    public static AgriClient getInstance() {
        if (mAgriClient == null) {
            synchronized (AgriClient.class) {//synchronized锁死
                if (mAgriClient == null) {
                    mAgriClient = new AgriClient();
                }
            }
        }
        return mAgriClient;
    }


    /**
     * 获取用户信息
     *
     * @param callback
     */
    public void Login(String username, String password, final AgriCallback<LoginBean> callback) {
        OkHttpUtils.post()
                .url(Constant.serverUrl + Constant.LOGIN_URL)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e, id);
                        Log.i(TAG, "onResponse: " + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i(TAG, "onResponse11: " + response);
                        LoginBean LoginDataBean = mGson.fromJson(response, LoginBean.class);
                        callback.onSuccess(LoginDataBean, id);
                    }
                });

    }
}
