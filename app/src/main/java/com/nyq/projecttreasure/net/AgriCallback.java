package com.nyq.projecttreasure.net;

/**
 * Created by Liu_xg on 2017/3/2.
 * 新闻数据回调接口
 */

public interface AgriCallback<T> {
    /**
     * 数据成功时候回调
     * @param response 成功回调接口
     * @param id       成功码
     */
    void onSuccess(T response, int id);

    /**
     * 数据失败时候回调
     * @param e    失败回调异常
     * @param id   失败码
     */
    void onError(Exception e, int id);


}
