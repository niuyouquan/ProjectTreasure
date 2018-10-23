package com.nyq.projecttreasure.application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nyq.projecttreasure.models.LoginBean;
import com.nyq.projecttreasure.utils.Constant;


public class ParseUserData {

    public LoginBean.DataBean parseUser(String json) {
        Gson gson = new Gson();
        LoginBean.DataBean u = gson.fromJson(json, new TypeToken<LoginBean.DataBean>() {
        }.getType());
        return u;
    }

    public LoginBean parseUserLoginAppData(String json) {
        LoginBean appData = new LoginBean();
        Gson gson = new Gson();
        appData.setData(gson.fromJson(json, new TypeToken<LoginBean.DataBean>() {
        }.getType()));
        return appData;
    }

    public LoginBean parseVivistAppData(String json) {
        LoginBean appData = new LoginBean();
        Gson gson = new Gson();

        appData.setData(gson.fromJson(json, new TypeToken<LoginBean.DataBean>() {
        }.getType()));
        return appData;
    }


    /**
     * 游客初始化
     *
     * @return
     */
    public LoginBean initVisitor() {
        LoginBean appData = new LoginBean();
        LoginBean.DataBean user = new LoginBean.DataBean();
        user.setId(Constant.VISTOR);
        user.setLogin_account(Constant.ACCOUNT);
        user.setUuid(Constant.VISTOR_TOKEN);
        appData.setData(user);
        return appData;
    }

}