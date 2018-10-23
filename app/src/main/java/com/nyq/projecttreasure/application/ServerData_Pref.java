package com.nyq.projecttreasure.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nyq.projecttreasure.models.LoginBean;

public class ServerData_Pref {
    private String TAG = "ServerData_Preference";
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private final String preferencesName = "ServerDataPreference";
    private Gson gson;

    private final String LoginBean = "LoginBean";

    public ServerData_Pref(Context context) {
        gson = new Gson();
        this.mContext = context;
        this.mSharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        this.editor = mSharedPreferences.edit();
    }


    /**
     * 儲存App Data
     *
     * @param:AppData date
     */
    public void saveAppData(LoginBean data) {
        LoginBean tempData = data;
        String jsonString = gson.toJson(tempData);
        editor.putString(LoginBean, jsonString);
        editor.commit();
    }


    /**
     * get App Data
     *
     * @param:AppData date
     */
    public LoginBean getAppData() {
        String temp = mSharedPreferences.getString(LoginBean, "");
        if (temp.length() == 0 || temp.isEmpty()) {
            return null;
        }
        LoginBean tempData = gson.fromJson(temp, LoginBean.class);
        return tempData;
    }

    /**
     * 清除 AppData
     */
    public void clearAppData() {
//        LogUtil.d("clearAppData", "clearAppData");
        editor.putString(LoginBean, null);
        editor.commit();
    }

}
